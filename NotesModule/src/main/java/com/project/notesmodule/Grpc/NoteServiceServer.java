package com.project.notesmodule.Grpc;

import com.google.protobuf.Empty;
import com.project.notesmodule.grpc.NotesServiceProto.Note;
import com.project.notesmodule.grpc.NotesServiceProto.NoteCreationRequest;
import com.project.notesmodule.grpc.NotesServiceProto.DeleteNoteResponse;
import com.project.notesmodule.grpc.NotesServiceProto.NoteIdRequest;
import com.project.notesmodule.grpc.NotesServiceProto.UpdateNoteRequest;
import com.project.notesmodule.grpc.NoteServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class NoteServiceServer {

    public static void main(String[] args) throws Exception {
        // Tạo server gRPC và gán service implementation
        Server server = ServerBuilder.forPort(50053)
                .addService(new NoteServiceImpl())
                .build();

        System.out.println("gRPC server is starting...");
        server.start();
        server.awaitTermination();
    }

    // Implement gRPC service
    public static class NoteServiceImpl extends NoteServiceGrpc.NoteServiceImplBase {

        @Override
        public void createNote(NoteCreationRequest request, StreamObserver<Note> responseObserver) {
            // Giả lập tạo ghi chú và trả về
            Note note = Note.newBuilder()
                    .setId("1")
                    .setTitle(request.getTitle())
                    .setContent(request.getContent())
                    .build();

            responseObserver.onNext(note);
            responseObserver.onCompleted();
        }

        @Override
        public void getNotes(Empty request, StreamObserver<Note> responseObserver) {
            // Giả lập trả về danh sách ghi chú
            Note note1 = Note.newBuilder().setId("1").setTitle("Note 1").setContent("Content 1").build();
            Note note2 = Note.newBuilder().setId("2").setTitle("Note 2").setContent("Content 2").build();

            responseObserver.onNext(note1);
            responseObserver.onNext(note2);
            responseObserver.onCompleted();
        }

        @Override
        public void getNote(NoteIdRequest request, StreamObserver<Note> responseObserver) {
            // Giả lập lấy ghi chú theo ID
            Note note = Note.newBuilder()
                    .setId(request.getId())
                    .setTitle("Example Title")
                    .setContent("Example Content")
                    .build();

            responseObserver.onNext(note);
            responseObserver.onCompleted();
        }

        @Override
        public void updateNote(UpdateNoteRequest request, StreamObserver<Note> responseObserver) {
            // Giả lập cập nhật ghi chú
            Note note = Note.newBuilder()
                    .setId(request.getId())
                    .setTitle(request.getNote().getTitle())
                    .setContent(request.getNote().getContent())
                    .build();

            responseObserver.onNext(note);
            responseObserver.onCompleted();
        }

        @Override
        public void deleteNote(NoteIdRequest request, StreamObserver<DeleteNoteResponse> responseObserver) {
            // Giả lập xóa ghi chú
            DeleteNoteResponse response = DeleteNoteResponse.newBuilder()
                    .setMessage("Note with ID " + request.getId() + " deleted successfully.")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
