$(document).ready(function () {
    
});

function Reportes(id) {
    //alert('lLEGAMOS A JS ' + id);
    $.ajax({
        url: "/FERRETERIA/ReportesMod",
        data: { bandera: id },
        type: 'POST',
        success: function (resp) {
            
        },
        error: function () {
            $("#error").show();
        }
    });
}