const { xemayModel } = require('../models/xemay_model')


exports.getListXeMay = async (req, res, next) => {
    try {
        let listXeMay = await xemayModel.find({})
        res.json(listXeMay)
    } catch (error) {
        res.json({ status: "lỗi", result: error })
    }
}

exports.addXeMay = async (req, res, next) => {
    try {
        const {file} = req;
        const imageUrl = `${req.protocol}://${req.get("host")}/uploads/${file.filename}`;

        // nếu ảnh là array thì:
        // const { files } = req
        // const urlsImage = files.map((file) => `${req.protocol}://${req.get("host")}/uploads/${file.filename}`);

        const data = req.body;
        const newXeMay = new xemayModel({
            ten_xe_ph41980: data.ten_xe_ph41980,
            mausac_ph41980: data.mausac_ph41980,
            gia_ban_ph41980: data.gia_ban_ph41980,
            mo_ta_ph41980: data.mo_ta_ph41980,
            hinh_anh_ph41980: imageUrl,
        })
        const saveXeMay = await newXeMay.save();
        res.status(201).json(saveXeMay)

    } catch (error) {
        res.json({ status: "lỗi", result: error })
    }
}
exports.updateXeMay = async (req, res, next) => {
    try {
        const { id } = req.params
        const { file } = req;
        if (!file) {
            return res.status(400).json({ message: 'Không có tệp được gửi' });
        }
        const imageUrl = `${req.protocol}://${req.get("host")}/uploads/${file.filename}`;
        // nếu ảnh là array thì:
        // const { files } = req
        // const urlsImage = files.map((file) => `${req.protocol}://${req.get("host")}/uploads/${file.filename}`)
        const data = req.body;
        const updateXeMay = {
            ten_xe_ph41980: data.ten_xe_ph41980,
            mausac_ph41980: data.mausac_ph41980,
            gia_ban_ph41980: data.gia_ban_ph41980,
            mo_ta_ph41980: data.hinh_anh_ph41980,
            hinh_anh_ph41980: imageUrl
        }
        let result = await xemayModel.findByIdAndUpdate(id, updateXeMay, {
            new: true,
        });

        if (result) {
            return res.status(200).json({
                status: 200,
                message: "Cập nhật xe máy thành công",
                data: result,
            });
        } else {
            return res.status(400).json({
                status: 400,
                message: "Không thể cập nhật xe máy",
                data: [],
            });
        }


    } catch (error) {
        console.log(error)
    }
}
exports.deleteXeMay = async (req, res) => {
    try {
        const deletedXeMay = await xemayModel.findByIdAndDelete(req.params.id);
        if (!deletedXeMay) {
            return res.status(404).json({ message: 'Không tìm thấy' });
        }
        res.status(200).json({ message: 'Xóa thành công' });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
}

exports.getXeMayById = async (req, res,next) => {
    try {
        const xemay = await xemayModel.findById(req.params.id);
       
        if (!xemay) {
            return res.status(404).json({ message: 'Sản phẩm không tồn tại' });
        }
         // Gửi phản hồi chứa dữ liệu sản phẩm đã tìm thấy
         res.status(200).json({ status: 200, data: xemay });
    } catch (error) {
        console.log(error)
    }
}


