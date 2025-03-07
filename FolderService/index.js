const GrpcService = require("./src/services/GrpcService.js");
const express = require("express");
const cors = require("cors");
const dotenv = require("dotenv");

const grpc = require("@grpc/grpc-js");
const protoLoader = require("@grpc/proto-loader");

// Đọc file .env
dotenv.config();

// Tải cấu trúc .proto cho gRPC
const packageDefinition = protoLoader.loadSync("./proto/service.proto"); //tỉa tệp proto là cấu trúc Protobuf
const proto = grpc.loadPackageDefinition(packageDefinition);

// Khởi tạo API Express
const app = express();
const apiPort = 3002;

// Middleware cho API
app.use(
  cors({
    origin: "http://localhost:5173", // URL frontend
    credentials: true, // Cho phép gửi cookie và thông tin xác thực
  })
);
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Dùng router cho các API
const router = require("./src/routes/index.js");
app.use("/api/folder", router);

app.get("/", (req, res) => {
  res.json({ message: "FolderService API is running" });
});

// Khởi tạo gRPC server
const grpcServer = new grpc.Server();
grpcServer.addService(proto.DataService.service, GrpcService);

// Hàm khởi động API server
const startApiServer = () => {
  return new Promise((resolve, reject) => {
    app.listen(apiPort, "0.0.0.0", () => {
      console.log(`API Server running on port ${apiPort}`);
      resolve();
    });
  });
};

// Hàm khởi động gRPC server
let hasNotifiedOnRestart = false;
const startGrpcServer = () => {
  return new Promise((resolve, reject) => {
    grpcServer.bindAsync(
      "0.0.0.0:50052",
      grpc.ServerCredentials.createInsecure(),
      (err, port) => {
        if (err) {
          reject(err);
        } else {
          console.log(`gRPC Server running on port ${port}`);
          GrpcService.notifyOtherNodesOnRestart();
          resolve();
        }
      }
    );
  });
};

// Khởi động cả hai server
const startServers = async () => {
  try {
    await Promise.all([startApiServer(), startGrpcServer()]);
    console.log("Both servers are running!");
  } catch (error) {
    console.error("Error starting servers:", error);
  }
};

// Khởi động cả hai server khi ứng dụng được khởi tạo
startServers();
