const grpc = require("@grpc/grpc-js");
const { readDb, writeDb } = require("../database/jsonDb");
const { v4: uuidv4 } = require("uuid");

const healthCheck = (call, callback) => {
  // Trả về trạng thái OK nếu hệ thống hoạt động bình thường
  try {
    callback(null, { message: "Service is healthy" });
  } catch (error) {
    // Trả về trạng thái ERR nếu hệ thống gặp sự cố
    callback({ message: "Service is unhealthy" });
  }
};

const GetUser = (call, callback) => {
  try {
    const { id } = call.request;

    if (!id) {
      // Nếu không có ID, trả về lỗi
      return callback({
        code: grpc.status.INVALID_ARGUMENT,
        message: "Id không hợp lệ",
      });
    }
    const db = readDb();
    const user = db.users.find((user) => {
      return user.id === id;
    });

    if (!user) {
      return callback({
        code: grpc.status.INVALID_ARGUMENT,
        message: "user không tồn tại",
      });
    }

    callback(null, { user });
  } catch (err) {
    console.error(err);
  }
};

const UpsertUser = (call, callback) => {
  const { id, name, email, password } = call.request;
  const db = readDb();

  const userIndex = db.users.findIndex((user) => user.id === id);
  if (userIndex === -1) {
    const newUser = { id, name, email, password };
    db.users.push(newUser);
    writeDb(db);
    return callback(null, { success: true, message: "Thêm user thành công" });
  }
  db.users[userIndex] = { id, name, email, password };
  return callback(null, {
    success: true,
    message: "User cập nhật thành công",
  });
};

const GetFolder = (call, callback) => {
  const { id } = call.request;
  if (!id) {
    return callback(null, {
      success: false,
      message: "Id không tồn tại",
    });
  }
  const db = readDb();
  const folder = db.folders.find((folder) => folder.id === id);
  if (!folder) {
    return callback(null, {
      success: false,
      message: "Folder không tồn tại",
    });
  }

  callback(null, {
    success: true,
    folder,
  });
};

const AddFolder = (call, callback) => {
  const { id, userId, name } = call.request;

  if (!userId) {
    return callback(null, {
      success: false,
      message: "Người dùng không tồn tại",
    });
  }

  const db = readDb();
  const folder = {
    id,
    userId,
    name,
  };
  db.folders.push(folder);
  writeDb(db);
  callback(null, { success: true, message: "Thêm folder thành công" });
};

const UpdateFolder = (call, callback) => {
  const { id, name } = call.request;
  const db = readDb();
  const folder = db.folders.find((folder) => folder.id === id);
  if (!folder) {
    return callback(null, { success: false, message: "Folder không tồn tại" });
  }
  folder.name = name;
  writeDb(db);
  callback(null, { success: true, message: "Folder update thành công" });
};

const DeleteFolder = (call, callback) => {
  const { id } = call.request;
  const db = readDb();
  db.folders = db.folders.filter((folder) => folder.id !== id);
  writeDb(db);
  callback(null, { success: true, message: "Xóa folder thành công" });
};

const GetNote = (call, callback) => {
  const { id } = call.request;
  if (!id) {
    return callback(null, {
      success: false,
      message: "Id không tồn tại",
    });
  }
  const db = readDb();
  const note = db.notes.find((note) => note.id === id);
  if (!note) {
    return callback(null, {
      success: false,
      message: "Note không tồn tại",
    });
  }

  callback(null, {
    success: true,
    note,
  });
};

const AddNote = (call, callback) => {
  const { id, folderId, content } = call.request;
  const db = readDb();
  const note = { id, folderId, content };
  db.notes.push(note);
  writeDb(db);
  callback(null, { success: true, message: "Thêm note thành công" });
};

// Cập nhật Note
const UpdateNote = (call, callback) => {
  const { id, content } = call.request;
  const db = readDb();
  const note = db.notes.find((note) => note.id === id);
  if (!note) {
    return callback(null, { success: false, message: "Note không tồn tại" });
  }
  note.content = content;
  writeDb(db);
  callback(null, { success: true, message: "Cập nhật note thành công" });
};

// Xóa Note
const DeleteNote = (call, callback) => {
  const { id } = call.request;
  const db = readDb();
  db.notes = db.notes.filter((note) => note.id !== id);
  writeDb(db);
  callback(null, { success: true, message: "Xóa note thành công" });
};

module.exports = {
  healthCheck,
  GetUser,
  UpsertUser,
  GetFolder,
  AddFolder,
  UpdateFolder,
  DeleteFolder,
  GetNote,
  AddNote,
  UpdateNote,
  DeleteNote,
};
