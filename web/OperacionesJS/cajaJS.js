$(document).ready(function () {
    (function ($) {
        $('#filtroCaja').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsCaja tr').hide();
            $('.RowsCaja tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_Caja();
});

function prepararJSON(ban) {
    JSON = {
        bandera: ban,
        id_caja: $('#id_caja').val(),
        caja: $('#caja').val()
    };
    enviarDatos();
}


function AgregarCaja() {
    prepararJSON(1);
}

function ModificarCaja() {
    prepararJSON(2);
}

function EliminarCaja() {
    prepararJSON(3);
}


function enviarDatos() {
    $.ajax({
        url: "/FERRETERIA/CajaMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarCampos();
            Cargar_Caja();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function Cargar_Caja() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CajaMod",
        data: {
            bandera: 4
        },
        success: function (valor) {
            $('#RowCaja').html(valor);

            $.each(valor, function (indice, value) {
                $("#RowCaja").append($("<tr>").append($("<td>" + value.id_caja + "</td>" + "<td>" + value.caja + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function LimpiarCaja() {
    $('#id_caja').val("");
    $('#caja').val("");

}

function Recuperar_Caja() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/CajaMod",
        data: {
            bandera: 5,
            id_caja: $('#id_caja').val()
        },
        success: function (json_caja) {
            //alert(json_caja);

            $.each(json_caja, function (key, value) {
                $('#id_caja').val(value.id_caja);
                $('#caja').val(value.caja);
                $('#caja').focus();
            });
        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}