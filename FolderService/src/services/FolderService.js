const bcrypt = require("bcryptjs");
const { readDb, writeDb } = require("../database/jsonDb");
const { v4: uuidv4 } = require("uuid");
const { NoteService, UserService } = require("../../client");

const folderService = {
  createFolder: (req, res) => {
    try {
      const { name, userId } = req.body;

      console.log(
        "🚀 ------------------------------------------------------------🚀"
      );
      console.log(
        "🚀 ~ file: FolderService.js:11 ~ name, userId:",
        name,
        userId
      );
      console.log(
        "🚀 ------------------------------------------------------------🚀"
      );

      const db = readDb();
      const folder = {
        id: uuidv4(),
        userId,
        name,
      };
      db.folders.push(folder);
      writeDb(db);
      NoteService.AddFolder(folder, (err, response) => {
        console.log("Error syncing with NoteService:", err);
      });
      UserService.AddFolder(folder, (err, response) => {
        console.log("Error syncing with FolderService:", err);
      });
      res.json({ success: true, message: "Folder created", folder });
    } catch (error) {
      console.error(error);
      res
        .status(500)
        .json({ success: false, message: "Error creating folder" });
    }
  },
  updateFolder: (req, res) => {
    try {
      const { id, name } = req.body;
      const db = readDb();
      const folder = db.folders.find((folder) => folder.id === id);
      if (!folder) {
        return res.status(404).json({ message: "Folder không tồn tại" });
      }
      folder.name = name;
      writeDb(db);
      NoteService.UpdateFolder(folder, (err, response) => {
        console.log("Lỗi cập nhật folder noteService:", err);
      });
      UserService.UpdateFolder(folder, (err, response) => {
        console.log("Lỗi cập nhật folder FolderService:", err);
      });
      res.json({ message: "Folder cập nhật thành công", folder });
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Lỗi cập nhật folder" });
    }
  },
  getFolder: async (req, res) => {
    try {
      const { id } = req.params;
      const db = readDb();
      let folder = db.folders.find((folder) => folder.id === id);
      if (!folder) {
        const userFolderPromise = new Promise((resolve, reject) => {
          UserService.GetFolder({ id }, (err, response) => {
            resolve(response.folder);
          });
        });
        const noteFolderPromise = new Promise((resolve, reject) => {
          NoteService.GetFolder({ id }, (err, response) => {
            resolve(response.folder);
          });
        });

        const [userFolder, noteFolder] = await Promise.all([
          userFolderPromise,
          noteFolderPromise,
        ]);
        folder = userFolder || noteFolder;
      }
      if (!folder) {
        return res.status(404).json({ message: "Folder không tồn tại" });
      }
      res.json({ folder });
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Lỗi lấy folder" });
    }
  },
  deleteFolder: (req, res) => {
    try {
      const { id } = req.params;
      const db = readDb();
      const folderIndex = db.folders.findIndex((folder) => folder.id === id);
      if (folderIndex === -1) {
        return res.status(404).json({ message: "Folder không tồn tại" });
      }
      db.folders.splice(folderIndex, 1);
      writeDb(db);

      NoteService.DeleteFolder({ id }, (err, response) => {
        console.log("Lỗi xóa folder noteService:", err);
      });

      UserService.DeleteFolder({ id }, (err, response) => {
        console.log("Lỗi xóa folder FolderService:", err);
      });
      res.json({ message: "Folder đã xóa" });
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Lỗi xóa folder" });
    }
  },
};

module.exports = folderService;
