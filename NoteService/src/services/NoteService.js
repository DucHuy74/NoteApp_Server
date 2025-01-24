const bcrypt = require("bcryptjs");
const { readDb, writeDb } = require("../database/jsonDb");
const { v4: uuidv4 } = require("uuid");
const { FolderService, UserService } = require("../../client");

const noteService = {
  createNote: (req, res) => {
    try {
      const { content, folderId } = req.body;
      const db = readDb();
      const note = {
        id: uuidv4(),
        content,
        folderId,
      };
      FolderService.AddNote(note, (err, response) => {
        if (err) {
          console.error("Error syncing with FolderService:", err);
        }
      });
      UserService.AddNote(note, (err, response) => {
        if (err) {
          console.error("Error syncing with UserService:", err);
        }
      });

      db.notes.push(note);
      writeDb(db);
      res.json({ message: "Thêm note thành công", note });
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Lỗi thêm note" });
    }
  },
  updateNote: (req, res) => {
    try {
      const { id, content } = req.body;
      const db = readDb();
      const note = db.notes.find((note) => {
        return note.id === id;
      });
      note.content = content;
      writeDb(db);
      if (!note) {
        return res.status(404).json({ message: "Note không tồn tại" });
      }
      FolderService.UpdateNote(note, (err, response) => {
        if (err) {
          console.error("Error syncing with FolderService:", err);
        }
      });
      UserService.UpdateNote(note, (err, response) => {
        if (err) {
          console.error("Error syncing with UserService:", err);
        }
      });

      res.json({ message: "Cập nhật note thành công", note });
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Lỗi cập nhật note" });
    }
  },
  getNote: async (req, res) => {
    try {
      const { id } = req.params;
      const db = readDb();
      let note = db.notes.find((note) => note.id === id);
      if (!note) {
        try {
          const userNotePromise = new Promise((resolve, reject) => {
            UserService.GetNote({ id }, (err, response) => {
              resolve(response.note);
            });
          });
          const folderNotePromise = new Promise((resolve, reject) => {
            FolderService.GetNote({ id }, (err, response) => {
              resolve(response.note);
            });
          });
          const [nodeUser, nodeFolder] = await Promise.all([
            userNotePromise,
            folderNotePromise,
          ]);
          note = nodeUser || nodeFolder;
        } catch (error) {
          console.error(error);
          res.status(500).json({ message: "Lỗi lấy note" });
        }
      }
      if (!note) {
        return res.status(404).json({ message: "Note không tồn tại" });
      }
      res.json({ note });
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Lỗi lấy note" });
    }
  },
  deleteNote: (req, res) => {
    try {
      const { id } = req.params;
      const db = readDb();
      const noteIndex = db.notes.findIndex((note) => note.id === id);
      if (noteIndex === -1) {
        return res.status(404).json({ message: "Note không tồn tại" });
      }
      db.notes.splice(noteIndex, 1);
      writeDb(db);
      FolderService.DeleteNote({ id }, (err, response) => {
        if (err) {
          console.error("Error syncing with FolderService:", err);
        }
      });
      UserService.DeleteNote({ id }, (err, response) => {
        if (err) {
          console.error("Error syncing with UserService:", err);
        }
      });
      res.json({ message: "Xóa note thành công" });
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Lỗi xóa note" });
    }
  },
};

module.exports = noteService;
