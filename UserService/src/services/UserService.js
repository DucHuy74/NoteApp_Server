const bcrypt = require("bcryptjs");
const { readDb, writeDb } = require("../database/jsonDb");
const { v4: uuidv4 } = require("uuid");
const { NoteService, FolderService } = require("../../client");

const userService = {
  createUser: (req, res) => {
    const { name, email, password, confirmPassword } = req.body;
    const reg = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    const isCheckEmail = reg.test(email);
    if (!email || !password || !confirmPassword) {
      return res.status(200).json({
        status: "ERR",
        message: "The input is required",
      });
    }
    if (!isCheckEmail) {
      return res.status(200).json({
        status: "ERR",
        message: "The input is email",
      });
    }
    if (password !== confirmPassword) {
      return res.status(200).json({
        status: "ERR",
        message: "The password is equal confirmPassword",
      });
    }

    const hash = bcrypt.hashSync(password, 10);

    const id = uuidv4();
    const db = readDb();
    const user = {
      id,
      name,
      email,
      password: hash,
    };
    db.users.push(user);
    writeDb(db);
    NoteService.UpsertUser(user, (err, response) => {
      if (err) {
        console.error("Error syncing with NoteService:", err);
      }
    });

    FolderService.UpsertUser(user, (err, response) => {
      if (err) {
        console.error("Error syncing with FolderService:", err);
      }
    });

    return res.status(200).json({
      status: "OK",
      message: "sign up success",
    });
  },
  signIn: async (req, res) => {
    const { email, password } = req.body;
    const db = readDb();
    const user = db.users.find((user) => {
      return user.email === email;
    });
    if (!bcrypt.compareSync(password, user.password)) {
      return res.status(200).json({
        status: "ERR",
        message: "Mật khẩu không đúng",
      });
    }
    const { password: userPassword, ...userResponse } = user;
    return res.status(200).json({
      status: "OK",
      message: "Đăng nhập thành công",
      user: userResponse,
    });
  },
  getDetailsUser: async (req, res) => {
    const { id } = req.params;

    if (!id) {
      return res.status(400).json({
        status: "ERR",
        message: "Thiếu id",
      });
    }

    const db = readDb();
    let user = db.users.find((user) => user.id === id);

    if (!user) {
      try {
        const noteUserPromise = new Promise((resolve, reject) => {
          NoteService.getUser({ id }, (err, response) => {
            resolve(response.user); 
          });
        });

        const folderUserPromise = new Promise((resolve, reject) => {
          FolderService.getUser({ id }, (err, response) => {
            resolve(response.user);
          });
        });

        const [noteUser, folderUser] = await Promise.all([
          noteUserPromise,
          folderUserPromise,
        ]);

        user = noteUser || folderUser;

        if (!user) {
          return res.status(404).json({
            status: "ERR",
            message: "Không tìm thấy user",
          });
        }
      } catch (err) {
        console.error("Error fetching user:", err);
        return res.status(500).json({
          status: "ERR",
          message: "Lỗi khi gọi service",
        });
      }
    }

    const { password: userPassword, ...userResponse } = user;
    return res.status(200).json({
      status: "OK",
      user: userResponse,
    });
  },
};

module.exports = userService;
