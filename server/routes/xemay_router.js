const Upload = require('../config/common/upload');
var express = require("express");
var router = express.Router();
const xemayCtl = require("../controllers/xemay_controller");
// http://localhost:3000/xemay/addxemay
router.post("/addxemay", Upload.single('hinh_anh_ph41980'), xemayCtl.addXeMay);
//nếu ảnh là array
// router.put("/addxemay", Upload.array('hinh_anh_ph41980', 5),xemayCtl.updateProduct);
// http://localhost:3000/xemay/getListxemay
router.get("/getListxemay", xemayCtl.getListXeMay);
// http://localhost:3000/xemay/updatexemay/
router.put("/updatexemay/:id", Upload.single('hinh_anh_ph41980'), xemayCtl.updateXeMay);
// nếu ảnh là array
// router.put("/updatexemay/:id", Upload.array('hinh_anh_ph41980', 5),xemayCtl.updateProduct);
// http://localhost:3000/xemay/deletexemay/
router.delete("/deletexemay/:id", xemayCtl.deleteXeMay);
// http://localhost:3000/xemay/getxemayById/
router.get("/getxemayById/:id", xemayCtl.getXeMayById);
module.exports = router;