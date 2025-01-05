const grpc = require("@grpc/grpc-js");

const healthCheck = (call, callback) => {
  // Trả về trạng thái OK nếu hệ thống hoạt động bình thường
  try {
    
    callback(null, { message: "Service is healthy" });
  } catch (error) {
    // Trả về trạng thái ERR nếu hệ thống gặp sự cố
    callback({ message: "Service is unhealthy" });
  }
};

module.exports = {
  healthCheck,
};
