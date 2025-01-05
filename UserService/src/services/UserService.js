const db = require("~/database/db");
const bcrypt = require("bcryptjs");
const { generateAccessToken, generateRefreshToken } = require("./JwtService");

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

    const sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
    db.query(sql, [name, email, hash], (err, result) => {
      if (err) {
        console.error("Error inserting user into database:", err);
        return res.status(500).json({ message: "Lỗi khi thêm người dùng" });
      }
      return res.status(200).json({ message: "Thêm người dùng thành công" });
    });
  },
  signIn: (req, res) => {
    const { email, password } = req.body;
    const sql = "SELECT * FROM users WHERE email = ?";
    db.query(sql, [email], async (err, result) => {
      if (err) {
        console.error("Error getting user from database:", err);
        return res.status(500).json({ message: "Lỗi khi lấy người dùng" });
      }
      if (result.length === 0) {
        return res.status(200).json({
          status: "ERR",
          message: "Email không tồn tại",
        });
      }

      const user = result[0];
      if (!bcrypt.compareSync(password, user.password)) {
        return res.status(200).json({
          status: "ERR",
          message: "Mật khẩu không đúng",
        });
      }

      const access_token = await generateAccessToken({
        id: user.id,
        isAdmin: user.isAdmin,
      });
      const refresh_token = await generateRefreshToken({
        id: user.id,
        isAdmin: user.isAdmin,
      });

      return res.status(200).json({
        status: "OK",
        message: "Đăng nhập thành công",
        access_token,
        refresh_token,
      });
    });
  },
};

module.exports = userService;
