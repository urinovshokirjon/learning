// $('#studentImage').bind('change', function () {
//     var filename = $("#studentImage").val();
//     if (/^\s*$/.test(filename)) {
//         $(".image-file-upload").removeClass('active');
//         $("#studentImageChosen").text("No file chosen...");
//     } else {
//         $(".image-file-upload").addClass('active');
//         $("#studentImageChosen").text(filename.replace("C:\\fakepath\\", ""));
//     }
// });

const fileUploadInputBtn = document.getElementById('file-upload-input-id');
const fileUploadImgTag = document.getElementById('file-upload-img-id');

fileUploadInputBtn.addEventListener('change', function () {
    const [file] = this.files
    if (file) {
        fileUploadImgTag.src = URL.createObjectURL(file)
    }
    // this.files[0].name
})
