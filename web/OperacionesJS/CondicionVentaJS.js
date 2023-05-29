$(document).ready(function () {
    (function ($) {
        $('#filtroCV').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsCV tr').hide();
            $('.RowsCV tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_Tabla();
});

function prepararJSON(ban) {
    JSON = {
        bandera: ban,
        id_cv: $('#id_cv').val(),
        descripcion: $('#descripcion').val(),
        plazo: $('#plazo').val()
    };
    enviarDatos();
}


function AgregarCV() {
    prepararJSON(1);
}

function ModificarCV() {
    prepararJSON(2);
}

function EliminarCV() {
    prepararJSON(3);
}

function enviarDatos() {
    $.ajax({
        url: "/FERRETERIA/CondicionVentaMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarCV();
            Cargar_Tabla();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function LimpiarCV() {
    $('#id_cv').val("");
    $('#descripcion').val("");
    $('#plazo').val("");
}

function Cargar_Tabla() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CondicionVentaMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $('#RowCV').html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowCV").append($("<tr>").append($("<td>" + value.id_cv + "</td>" + "<td>" + value.descripcion + "</td>" +
                        "<td>" + value.plazo + "</td>")));
            });
        },
        error: function (e) {
            alert('Error al Cargar Tabla.: ' + e);
        }
    });
}

function RecuperarCV() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CondicionVentaMod",
        data: {
            bandera: 5,
            id_cv: $('#id_cv').val()
        },
        success: function (json_cv) {
            //alert(json_cv);

            $.each(json_cv, function (key, value) {
                $('#id_cv').val(value.id_cv);
                $('#descripcion').val(value.descripcion);
                $('#plazo').val(value.plazo);

                $('#descripcion').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}

function Condicion() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CondicionVentaMod",
        data: {
            bandera: 6
        },
        success: function (cond) {
            $('#id_cv').html(cond);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}