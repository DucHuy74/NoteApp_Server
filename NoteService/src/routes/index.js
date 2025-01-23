const express = require("express");
const router = express.Router();
const noteService = require("../services/NoteService.js");

router.post("/create", noteService.createNote);
router.post("/update", noteService.updateNote);
router.get("/get/:id", noteService.getNote);
router.delete("/delete/:id", noteService.deleteNote);

module.exports = router;
