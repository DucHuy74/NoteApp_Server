const express = require("express");
const router = express.Router();
const userService = require("../services/UserService.js");

router.post("/sign-up", userService.createUser);
router.post("/sign-in", userService.signIn);

module.exports = router;
