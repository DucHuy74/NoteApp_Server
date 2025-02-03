const express = require("express");
const router = express.Router();
const folderService = require("../services/FolderService.js");

router.post("/create", folderService.createFolder);
router.put("/update", folderService.updateFolder);
router.get("/get/:id", folderService.getFolder);
router.delete("/delete/:id", folderService.deleteFolder);
router.get("/get-all/:userId", folderService.getAllFolder);

module.exports = router;
