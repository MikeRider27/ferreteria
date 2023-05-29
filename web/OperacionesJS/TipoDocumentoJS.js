$(document).ready(function () {
    (function ($) {
        $('#filtroDoc').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsDoc tr').hide();
            $('.RowsDoc tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_Tabla();
});

function prepararJSON(ban) {
    JSON = {
        bandera: ban,
        id_documento: $('#id_documento').val(),
        documento: $('#documento').val()
    };
    enviarDatos();
}


function AgregarDoc() {
    prepararJSON(1);
}

function ModificarDoc() {
    prepararJSON(2);
}

function EliminarDoc() {
    prepararJSON(3);
}

function enviarDatos() {
    $.ajax({
        url: "/FERRETERIA/TipoDocumentoMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarTP();
            Cargar_Tabla();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function LimpiarTP() {
    $('#id_documento').val("");
    $('#documento').val("");
}

function Cargar_Tabla() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/TipoDocumentoMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $('#RowDocumento').html(parametro);
            
            $.each(parametro, function (indice, value) {
                $("#RowDocumento").append($("<tr>").append($("<td>" + value.id_documento + "</td>" + "<td>" + value.documento + "</td>")));
            });
        },
        error: function (e) {
            alert('Error al Cargar Tabla.: ' + e);
        }
    });
}

function RecuperarTD() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/TipoDocumentoMod",
        data: {
            bandera: 5,
            id_documento: $('#id_documento').val()
        },
        success: function (json_documento) {
            alert(json_documento);

            $.each(json_documento, function (key, value) {
                $('#id_documento').val(value.id_documento);
                $('#documento').val(value.documento);

                $('#documento').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}

function Documento() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/TipoDocumentoMod",
        data: {
            bandera: 6
        },
        success: function (doc) {
            $('#id_documento').html(doc);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}