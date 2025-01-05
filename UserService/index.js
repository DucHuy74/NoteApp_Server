const GrpcService = require("./src/services/GrpcService");
const express = require("express");
const cors = require("cors");
const dotenv = require("dotenv");
const grpc = require("@grpc/grpc-js");
const protoLoader = require("@grpc/proto-loader");
const redis = require("redis");
require("module-alias/register");

// Đọc file .env
dotenv.config();

// Tải cấu trúc .proto cho gRPC
const packageDefinition = protoLoader.loadSync("./proto/userServices.proto"); //tỉa tệp proto là cấu trúc Protobuf
const proto = grpc.loadPackageDefinition(packageDefinition);

// Khởi tạo Redis Client
const redisClient = redis.createClient({
  host: process.env.REDIS_HOST || "localhost",
  port: process.env.REDIS_PORT || 6379,
});

redisClient.on("connect", () => {
  console.log("Connected to Redis");
});

// Khởi tạo API Express
const app = express();
const apiPort = 3001;

// Middleware cho API
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Dùng router cho các API
const router = require("./src/routes/index.js");
app.use("/api/user", router);

app.get("/", (req, res) => {
  res.json({ message: "UserService API is running" });
});

// Khởi tạo gRPC server
const grpcServer = new grpc.Server();
grpcServer.addService(proto.UserService.service, GrpcService);

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
const startGrpcServer = () => {
  return new Promise((resolve, reject) => {
    grpcServer.bindAsync(
      "0.0.0.0:50051",
      grpc.ServerCredentials.createInsecure(),
      (err, port) => {
        if (err) {
          reject(err);
        } else {
          console.log(`gRPC Server running on port ${port}`);
          //   grpcServer.start();//Việc gọi start đã được sử lý khi bindAsync().
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

// Hàm sao lưu dữ liệu vào Redis
function backupData(call, callback) {
  const nodeType = call.request.node_type;
  const data = call.request.data;

  // Lưu bản sao dữ liệu vào Redis
  redisClient.set(`${nodeType}_backup`, data, (err, reply) => {
    if (err) {
      callback({
        success: false,
        message: `Error storing backup: ${err}`,
      });
    } else {
      callback({
        success: true,
        message: `Backup for ${nodeType} saved successfully.`,
      });
    }
  });
}

// Hàm phục hồi dữ liệu từ Redis
function retrieveBackup(call, callback) {
  const nodeType = call.request.node_type;

  // Lấy bản sao dữ liệu từ Redis
  redisClient.get(`${nodeType}_backup`, (err, data) => {
    if (err || !data) {
      callback({
        success: false,
        message: `No backup found for ${nodeType}.`,
      });
    } else {
      callback({
        success: true,
        message: `Backup data retrieved successfully.`,
        data: data,
      });
    }
  });
}
