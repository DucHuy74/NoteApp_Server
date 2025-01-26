//package com.project.foldermodule.grpc_server;
//
//import com.google.protobuf.Empty;
//import com.project.foldermodule.grpc.FolderServiceProto.Folder;
//import com.project.foldermodule.grpc.FolderServiceProto.FolderCreationRequest;
//import com.project.foldermodule.grpc.FolderServiceProto.FolderIdRequest;
//import com.project.foldermodule.grpc.FolderServiceProto.FolderUpdateRequest;
//import com.project.foldermodule.grpc.FolderServiceProto.DeleteFolderResponse;
//import com.project.foldermodule.grpc.FolderServiceProto.GetFoldersResponse;
//import com.project.foldermodule.grpc.FolderServiceGrpc;
//import com.project.foldermodule.service.FolderService;
//import io.grpc.Server;
//import io.grpc.ServerBuilder;
//import io.grpc.stub.StreamObserver;
//
//import java.time.LocalDate;
//
//public class FolderServiceServer {
//
//    public static void main(String[] args) throws Exception {
//        // Tạo server gRPC và gán service implementation
//        Server server = ServerBuilder.forPort(50052)
//                .addService(new FolderServiceImpl())
//                .build();
//
//        System.out.println("gRPC server is starting...");
//        server.start();
//        server.awaitTermination();
//    }
//
//    // Implement FolderService gRPC
//    public static class FolderServiceImpl extends FolderServiceGrpc.FolderServiceImplBase {
//
//        private final FolderService folderService;
//
//        public FolderServiceImpl() {
//            // Tạo instance FolderService giả lập (hoặc inject từ Spring nếu cần)
//            this.folderService = new FolderService();
//        }
//
//        @Override
//        public void createFolder(FolderCreationRequest request, StreamObserver<Folder> responseObserver) {
//            // Lấy ngày hiện tại
//            LocalDate currentDate = LocalDate.now();
//
//            // Giả lập tạo thư mục và trả về
//            Folder folder = Folder.newBuilder()
//                    .setId("1")
//                    .setFolderName(request.getFolderName())
//                    .setCreateDate(currentDate.toString())  // Sử dụng ngày hiện tại
//                    .setUpdatedAt(currentDate.toString())  // Sử dụng ngày hiện tại
//                    .build();
//
//            // Gửi kết quả và hoàn thành
//            responseObserver.onNext(folder);
//            responseObserver.onCompleted();
//        }
//
//        @Override
//        public void getFolders(Empty request, StreamObserver<GetFoldersResponse> responseObserver) {
//            // Giả lập trả về danh sách thư mục
//            Folder folder1 = Folder.newBuilder().setId("1").setFolderName("Folder 1").setCreateDate(LocalDate.now().toString()).setUpdatedAt(LocalDate.now().toString()).build();
//            Folder folder2 = Folder.newBuilder().setId("2").setFolderName("Folder 2").setCreateDate(LocalDate.now().toString()).setUpdatedAt(LocalDate.now().toString()).build();
//
//            GetFoldersResponse response = GetFoldersResponse.newBuilder()
//                    .addFolders(folder1)
//                    .addFolders(folder2)
//                    .build();
//
//            responseObserver.onNext(response);
//            responseObserver.onCompleted();
//        }
//
//        @Override
//        public void getFolder(FolderIdRequest request, StreamObserver<Folder> responseObserver) {
//            // Giả lập lấy thư mục theo ID
//            Folder folder = Folder.newBuilder()
//                    .setId(request.getFolderId())
//                    .setFolderName("Example Folder")
//                    .setCreateDate(LocalDate.now().toString())  // Sử dụng ngày hiện tại
//                    .setUpdatedAt(LocalDate.now().toString())  // Sử dụng ngày hiện tại
//                    .build();
//
//            responseObserver.onNext(folder);
//            responseObserver.onCompleted();
//        }
//
//        @Override
//        public void updateFolder(FolderUpdateRequest request, StreamObserver<Folder> responseObserver) {
//            // Lấy ngày hiện tại
//            LocalDate currentDate = LocalDate.now();
//
//            // Giả lập cập nhật thư mục
//            Folder folder = Folder.newBuilder()
//                    .setId(request.getFolderId())
//                    .setFolderName(request.getFolderName())
//                    .setCreateDate(LocalDate.now().toString())  // Sử dụng ngày hiện tại
//                    .setUpdatedAt(currentDate.toString())  // Cập nhật ngày hiện tại
//                    .build();
//
//            responseObserver.onNext(folder);
//            responseObserver.onCompleted();
//        }
//
//        @Override
//        public void deleteFolder(FolderIdRequest request, StreamObserver<DeleteFolderResponse> responseObserver) {
//            // Giả lập xóa thư mục
//            DeleteFolderResponse response = DeleteFolderResponse.newBuilder()
//                    .setMessage("Folder with ID " + request.getFolderId() + " deleted successfully.")
//                    .build();
//
//            responseObserver.onNext(response);
//            responseObserver.onCompleted();
//        }
//    }
//}