$(document).ready(function () {
    (function ($) {
        $('#filtroPerfil').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsPerfil tr').hide();
            $('.RowsPerfil tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_Tabla();
});

function prepararJSON(ban) {
    JSON = {
        bandera: ban,
        id_perfil: $('#id_perfil').val(),
        perfil: $('#perfil').val()
    };
    enviarDatos();
}


function AgregarPerfil() {
    prepararJSON(1);
}

function ModificarPerfil() {
    prepararJSON(2);
}

function EliminarPerfil() {
    prepararJSON(3);
}

function enviarDatos() {
    $.ajax({
        url: "/FERRETERIA/MantenerPerfilesMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarPerfiles();
            Cargar_Tabla();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}


function LimpiarPerfiles() {
    $('#id_perfil').val("");
    $('#perfil').val("");
}

function Cargar_Tabla() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/MantenerPerfilesMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $('#RowPerfiles').html(parametro);
            
            $.each(parametro, function (indice, value) {
                $("#RowPerfiles").append($("<tr>").append($("<td>" + value.id_perfil + "</td>" + "<td>" + value.perfil + "</td>")));
            });
        },
        error: function (e) {
            alert('Error al Cargar Tabla.: ' + e);
        }
    });
}

function RecuperarPerfil() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/MantenerPerfilesMod",
        data: {
            bandera: 5,
            id_perfil: $('#id_perfil').val()
        },
        success: function (json_perfil) {
           // alert(json_perfil);

            $.each(json_perfil, function (key, value) {
                $('#id_perfil').val(value.id_perfil);
                $('#perfil').val(value.perfil);

                $('#perfil').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}