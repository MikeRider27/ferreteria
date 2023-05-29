$(document).ready(function () {
    (function ($) {
        $('#filtroNacionalidad').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsNacionalidad tr').hide();
            $('.RowsNacionalidad tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_Tabla();
});

function prepararJSON(ban) {
    JSON = {
        bandera: ban,
        id_nacionalidad: $('#id_nacionalidad').val(),
        nacionalidad: $('#nacionalidad').val()
    };
    enviarDatos();
}


function AgregarNacionalidad() {
    prepararJSON(1);
}

function ModificarNacionalidad() {
    prepararJSON(2);
}

function EliminarNacionalidad() {
    prepararJSON(3);
}

function enviarDatos() {
    $.ajax({
        url: "/FERRETERIA/NacionalidadMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarCamposNAC();
            Cargar_Tabla();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function LimpiarCamposNAC() {
    $('#id_nacionalidad').val("");
    $('#nacionalidad').val("");

}

function Cargar_Tabla() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/NacionalidadMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $('#RowNac').html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowNac").append($("<tr>").append($("<td>" + value.id_nacionalidad + "</td>" + "<td>" + value.nacionalidad + "</td>")));
            });
        },
        error: function (e) {
            alert('Error al Cargar Tabla.: ' + e);
        }
    });
}

function RecuperarNacionalidad() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/NacionalidadMod",
        data: {
            bandera: 5,
            id_nacionalidad: $('#id_nacionalidad').val()
        },
        success: function (json_nac) {
            //alert(json_nac);

            $.each(json_nac, function (key, value) {
                $('#id_nacionalidad').val(value.id_nacionalidad);
                $('#nacionalidad').val(value.nacionalidad);

                $('#nacionalidad').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}