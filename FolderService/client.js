const grpc = require("@grpc/grpc-js");
const protoLoader = require("@grpc/proto-loader");

const packageDefinition = protoLoader.loadSync("./proto/service.proto"); //tỉa tệp proto là cấu trúc Protobuf
const proto = grpc.loadPackageDefinition(packageDefinition);

const NoteService = new proto.DataService(
  "localhost:50053", // Địa chỉ server
  grpc.credentials.createInsecure() // Không dùng SSL
);

const UserService = new proto.DataService(
  "localhost:50051", // Địa chỉ server
  grpc.credentials.createInsecure() // Không dùng SSL
);

module.exports = {
  NoteService,
  UserService,
};
