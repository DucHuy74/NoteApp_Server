const grpc = require("@grpc/grpc-js");
const protoLoader = require("@grpc/proto-loader");

const packageDefinition = protoLoader.loadSync("./proto/service.proto", {
  keepCase: true,
  longs: String,
  enums: String,
  defaults: true,
  oneofs: true,
});
const proto = grpc.loadPackageDefinition(packageDefinition);

const NoteService = new proto.DataService(
  "localhost:50053",
  grpc.credentials.createInsecure()
);

const FolderService = new proto.DataService(
  "localhost:50052",
  grpc.credentials.createInsecure()
);

module.exports = {
  NoteService,
  FolderService,
};
