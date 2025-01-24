const grpc = require("@grpc/grpc-js");
const { readDb, writeDb } = require("../database/jsonDb");
const { v4: uuidv4 } = require("uuid");
const { clients } = require("../../client");

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

const NODE_ID = "NoteService";
const OTHER_NODES = ["UserService", "FolderService"];

const HealthCheck = (call, callback) => {
  callback(null, { isHealthy: true });
};

const NodeRestarted = (call, callback) => {
  const { nodeId } = call.request;

  console.log(`Node ${nodeId} vừa bật lại. Gửi toàn bộ DB.`);
  const db = readDb(); // Đọc DB từ file JSON

  // Đảm bảo db.users, db.folders, và db.notes chứa các đối tượng chi tiết
  const dbResponse = {
    users: db.users || [],
    folders: db.folders || [],
    notes: db.notes || [],
  };

  callback(null, dbResponse);
};

const checkNodeStatus = () => {
  clients.forEach((client, index) => {
    client.HealthCheck({}, (err, response) => {
      if (err) {
        console.log(`Node ${OTHER_NODES[index]} không khả dụng.`);
      } else if (response.isHealthy) {
        console.log(`Node ${OTHER_NODES[index]} đang hoạt động.`);
      }
    });
  });
};

const db = readDb();
let isNotified = false;
const notifyOtherNodesOnRestart = () => {
  if(isNotified) return;
  clients.forEach((client, index) => {
    client.NodeRestarted({ nodeId: NODE_ID }, (err, response) => {

      if (err) {
        console.error(
          `Không thể thông báo cho Node ${OTHER_NODES[index]}:`,
          err.message
        );
      } else {
        // Kiểm tra dữ liệu trả về từ node (users, folders, notes)
        if (
          response &&
          (response.users || response.folders || response.notes)
        ) {
          console.log(`Node ${OTHER_NODES[index]} trả về dữ liệu thiếu:`);

          if(response.users && response.users.length > db.users.length) {
            console.log("Có users mới");
            db.users = response.users;
          }
          if(response.folders && response.folders.length > db.folders.length) {
            console.log("Có folders mới");
            db.folders = response.folders;
          }
          if(response.notes && response.notes.length > db.notes.length) {
            console.log("Có notes mới");
            db.notes = response.notes;
          }

          // Sau khi đồng bộ xong, lưu lại vào DB
          writeDb(db);
        } else {
          console.warn("Dữ liệu trả về không hợp lệ:", response);
        }
      }
    });
  });
  isNotified = true;
};

module.exports = {
  HealthCheck,
  NodeRestarted,
  checkNodeStatus,
  notifyOtherNodesOnRestart,
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
