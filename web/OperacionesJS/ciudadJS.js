$(document).ready(function () {
    (function ($) {
        $('#filtroCiudad').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsCiudad tr').hide();
            $('.RowsCiudad tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_Tabla();
});

function prepararJSON(ban) {
    JSON = {
        bandera: ban,
        id_ciudad: $('#id_ciudad').val(),
        ciudad: $('#ciudad').val()
    };
    enviarDatos();
}

function AgregarCiudad() {
    prepararJSON(1);
}

function ModificarCiudad() {
    prepararJSON(2);
}

function EliminarCiudad() {
    prepararJSON(3);
}

function enviarDatos() {
    $.ajax({
        url: "/FERRETERIA/CiudadMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarCiudades();
            Cargar_Tabla();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function LimpiarCiudades() {
    $('#id_ciudad').val("");
    $('#ciudad').val("");
}

function Cargar_Tabla() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CiudadMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $('#RowCiudades').html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowCiudades").append($("<tr>").append($("<td>" + value.id_ciudad + "</td>" + "<td>" + value.ciudad + "</td>")));
            });
        },
        error: function (e) {
            alert('Error al Cargar Tabla.: ' + e);
        }
    });
}

function RecuperarCiudad() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CiudadMod",
        data: {
            bandera: 5,
            id_ciudad: $('#id_ciudad').val()
        },
        success: function (json_ciudad) {
            //alert(json_ciudad);
            $.each(json_ciudad, function (key, value) {
                $('#id_ciudad').val(value.id_ciudad);
                $('#ciudad').val(value.ciudad);
                
                $('#ciudad').focus();
            });
        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}