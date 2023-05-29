$(document).ready(function () {
    (function ($) {
        $('#filtroCargo').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsCargo tr').hide();
            $('.RowsCargo tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_Tabla();
});

function prepararJSON(ban) {
    JSON = {
        bandera: ban,
        id_cargo: $('#id_cargo').val(),
        cargo: $('#cargo').val()
    };
    enviarDatos();
}


function AgregarCargo() {
    prepararJSON(1);
}

function ModificarCargo() {
    prepararJSON(2);
}

function EliminarCargo() {
    prepararJSON(3);
}

function enviarDatos() {
    $.ajax({
        url: "/FERRETERIA/cargoMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarCargos();
            Cargar_Tabla();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function LimpiarCargos() {
    $('#id_cargo').val("");
    $('#cargo').val("");

}

function Cargar_Tabla() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/cargoMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $('#RowCargo').html(parametro);

            $.each(parametro, function (indice, value) {
                $("#RowCargo").append($("<tr>").append($("<td>" + value.id_cargo + "</td>" + "<td>" + value.cargo + "</td>")));
            });
        },
        error: function (e) {
            alert('Error al Cargar Tabla.: ' + e);
        }
    });
}

function Recuperar_Cargo() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/cargoMod",
        data: {
            bandera: 5,
            id_cargo: $('#id_cargo').val()
        },
        success: function (json_cargo) {
            $.each(json_cargo, function (ind, value) {
                $('#id_cargo').val(value.id_cargo);
                $('#cargo').val(value.cargo);
                $('#cargo').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}