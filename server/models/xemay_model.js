const db = require("./db");
const xemay_Schema = new db.mongoose.Schema(
  {
    ten_xe_ph41980: {
      type: String,
      required: true,
    },

    mausac_ph41980: {
      type: String,
      required: true,
    },
    gia_ban_ph41980: {
      type: Number,
      required: true,
    },

    mo_ta_ph41980: {
      type: String,

    },
    hinh_anh_ph41980: {
      type: String,
      
    },
  },
  {
    collection: "xemays",
    timestamps: true,
  }
);
const xemayModel = db.mongoose.model("xemayModel", xemay_Schema);
module.exports = { xemayModel };