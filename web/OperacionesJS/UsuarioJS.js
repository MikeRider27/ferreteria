$(document).ready(function () {
    (function ($) {
        $('#filtroUsuario').keyup(function () {
            var rex = new RegExp($(this).val(), 'i');
            $('.RowsUsuario tr').hide();
            $('.RowsUsuario tr').filter(function () {
                return rex.test($(this).text());
            }).show();
        });
    }(jQuery));
    Cargar_TablaUsuario();
    CargarPerfiles();
    CargarCaja();
    CargarEmpleado();
    USUARIOhtml();
});

function mostrarModalEmpleado(mostrar) {
    if (mostrar) {
        document.getElementById('modal_buscador_empleado').style.display = 'block';
    } else {
        document.getElementById('modal_buscador_empleado').style.display = 'none';
    }
}

function recorrerTblBusEmpleado() {
    var tableOb, trOb;
    tableOb = document.getElementById('datos_empleados');
    if (!tableOb)
        return;
    trOb = tableOb.getElementsByClassName("opcion_seleccion_empleado");
    for (var x = 0; x < trOb.length; x++) {
        if (trOb[x].checked) {
            $('#id_empleado').val(trOb[x].id);
            mostrarModalEmpleado(false);
        }
    }
}

function prepararJSONUsuarios(ban) {
   // alert("Bandera " + ban);
    JSON = {
        bandera: ban,
        id_usuario: $('#id_usuario').val(),
        usuario: $('#usuario').val(),
        clave: $('#pass').val(),
        id_perfil: $('#id_perfil').val(),
        id_caja: $('#id_caja').val(),
        id_empleado: $('#id_empleado').val(),
        empleado: $('#empleado').val()
    }
    ;
    enviarUsuario();
}


function AgregarUsuario() {
    prepararJSONUsuarios(1);
}

function ModificarUsuario() {
    prepararJSONUsuarios(2);
}

function EliminarUsuario() {
    prepararJSONUsuarios(3);
}

function enviarUsuario() {
    $.ajax({
        url: "/FERRETERIA/UsuarioMod",
        data: JSON,
        type: 'POST',
        success: function () {
            LimpiarCamposUsuario();
            Cargar_TablaUsuario();
            USUARIOhtml();
        },
        error: function () {
            //alert('No se pudo obtener ultimo valor...!!!');
        }
    });
}

function Cargar_TablaUsuario() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/UsuarioMod",
        data: {
            bandera: 4
        },
        success: function (parametro) {
            $("#USER").html(parametro);

            $.each(parametro, function (indice, value) {
                $("#USER").append($("<tr>").append($("<td>" + value.id_usuario + "</td>"
                        + "<td>" + value.usuario + "</td>" + "<td>" + value.id_empleado + "</td>"
                        + "<td>" + value.id_perfil + "</td>" + "<td>" + value.id_caja + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function USUARIOhtml() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/UsuarioMod",
        data: {
            bandera: 9
        },
        success: function (lk) {
            $("#userHTML").html(lk);

            $.each(lk, function (indice, value) {
                $("#userHTML").append($("<tr>").append($("<td>" + value.apertura + "</td>" + "<td>" + value.fecha + "</td>"
                        + "<td>" + value.hora1 + "</td>" + "<td>" + value.hora2 + "</td>" + "<td>" + value.monto1 + "</td>" + "<td>" + value.monto2 + "</td>"
                        + "<td>" + value.ca + "</td>" + "<td>" + value.us + "</td>")));
            });
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarPerfiles() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/UsuarioMod",
        data: {
            bandera: 6
        },
        success: function (opcionesPerfil) {
            $("#id_perfil").html(opcionesPerfil);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarCaja() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/UsuarioMod",
        data: {
            bandera: 7
        },
        success: function (opCaja) {
            $('#id_caja').html(opCaja);
        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function CargarEmpleado() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/UsuarioMod",
        data: {
            bandera: 8
        },
        success: function (opEmpleado) {
            $('#id_empleado').html(opEmpleado);

        },
        error: function (e) {
            //alert('Error al Cargar Tabla.: '+e);
        }
    });
}

function LimpiarCamposUsuario() {
    $('#id_usuario').val("");
    $('#usuario').val("");
    $('#pass').val("");
    $('#id_perfil').val("");
    $('#id_caja').val("");
    $('#id_empleado').val("");
    $('#empleado').val("");
}

function Recuperar() {
    $.ajax({
        type: 'POST',
        url: "/FERRETERIA/UsuarioMod",
        data: {
            bandera: 5,
            id_usuario: $('#id_usuario').val()
        },
        success: function (json_Usuario) {
            $.each(json_Usuario, function (key, value) {
                $('#id_usuario').val(value.id_usuario);
                $('#usuario').val(value.usuario);
                $('#pass').val(value.clave);
                $('#id_empleado').val(value.id_empleado);
                $('#empleado').val(value.empleado);
                $('#id_perfil> option[value=' + value.id_perfil + ']').attr('selected', 'selected');
                $('#id_caja> option[value=' + value.id_caja + ']').attr('selected', 'selected');


                $('#usuario').focus();
            });

        },
        error: function (e) {
            alert('Error Durante la Recuperacion del Registro...: ');
        }
    });
}