const mongoose = require("mongoose");
mongoose
  .connect("mongodb+srv://xuong:nhom7@cluster0.drtagx5.mongodb.net/testthi", {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => {
    console.log("đã kết nối thành công");
  })
  .catch((err) => {
    console.error("lỗi kết nối " + err);
  });
module.exports = mongoose;