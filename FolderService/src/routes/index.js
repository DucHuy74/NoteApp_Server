const express = require("express");
const router = express.Router();
const folderService = require("../services/FolderService.js");

router.post("/create", folderService.createFolder);
router.post("/update", folderService.updateFolder);
router.get("/get/:id", folderService.getFolder);
router.delete("/delete/:id", folderService.deleteFolder);

module.exports = router;
