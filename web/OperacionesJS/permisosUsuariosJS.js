$(document).ready(function () {
   // login();
});

function Ayuda1() {
    $.ajax({
        url: "/FERRETERIA/MostrarAyuda",
        type: 'POST'
    });
}

function armarJSON() {
    datosJSON = {
        par_usuario: $('#dat_usuario').val(),
        par_clave: $('#dat_clave').val()
    };
    enviarUser();
}

function login() {
    armarJSON();
}

function enviarUser() {
    $.ajax({
        url: "/FERRETERIA/permisosUsuariosMod",
        data: datosJSON,
        type: 'POST',
        success: function (resp) {
           limpiarUser();
            $("#id01").hide();
            //document.getElementById('id01').style.display = 'none';
            $("#bar_menu_principal").html(resp);
        },
        error: function () {
            $("#error").show();
        }
    });
}

function limpiarUser() {
    $('#dat_usuario').val("");
    $('#dat_clave').val("");
}