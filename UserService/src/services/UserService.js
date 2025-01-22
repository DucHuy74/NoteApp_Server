const bcrypt = require("bcryptjs");
const { readDb, writeDb } = require("../database/jsonDb");
const { v4: uuidv4 } = require("uuid");

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
    } else if (!isCheckEmail) {
      return res.status(200).json({
        status: "ERR",
        message: "The input is email",
      });
    } else if (password !== confirmPassword) {
      return res.status(200).json({
        status: "ERR",
        message: "The password is equal confirmPassword",
      });
    }

    const hash = bcrypt.hashSync(password, 10);

    const id = uuidv4();
    const db = readDb();

    db.users.push({
      id,
      name,
      email,
      password: hash,
    });
    writeDb(db);
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
    const user = db.users.find((user) => user.id === id);
    if (!user) {
      return res.status(400).json({
        status: "ERR",
        message: "id người dùng không hợp lệ",
      });
    }
    const { password: userPassword, ...userResponse } = user;
    return res.status(200).json({
      status: "OK",
      user: userResponse,
    });
  },
};

module.exports = userService;
