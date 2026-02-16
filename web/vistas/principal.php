<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Red Social</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, sans-serif;
        }

        body {
            background-color: #18191a;
            color: #e4e6eb;
        }

        .pagina {
            display: grid;
            grid-template-columns: 1fr 320px;
            grid-template-rows: 70px 1fr;
            height: 100vh;
        }

        .barra-superior {
            grid-column: 1 / 3;
            background-color: #242526;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 40px;
            border-bottom: 1px solid #3a3b3c;
            position: sticky;
            top: 0;
        }

        .logo {
            display: flex;
            align-items: center;
            text-decoration: none;
        }

        .logo img {
            width: 50px;
            height: 50px;
            border-radius: 8px;
            transition: 0.2s;
        }

        .logo img:hover {
            opacity: 0.8;
            transform: scale(1.05);
        }


        .tituloapp {
            color: #2d88ff;
            font-weight: bold;
            font-size: 18px;
        }

        .tituloapp:hover {
            color: white;
            font-weight: bold;
            font-size: 18px;
        }

        .contenido-principal {
            padding: 40px;
            display: flex;
            justify-content: center;
            overflow-y: auto;
        }

        .tarjeta-publicacion {
            background-color: #242526;
            width: 600px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
            padding: 20px;
            border: 1px solid #3a3b3c;
        }

        .cabecera-publicacion {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        .foto-usuario {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            background-color: #3a3b3c;
            margin-right: 12px;
        }

        .nombre-usuario {
            font-weight: 600;
            color: #e4e6eb;
        }

        .contenido-publicacion {
            margin: 15px 0;
            font-size: 15px;
            line-height: 1.5;
            color: #d0d2d6;
        }

        .imagen-publicacion {
            width: 100%;
            height: 300px;
            background-color: #3a3b3c;
            border-radius: 10px;
        }

        .acciones {
            display: flex;
            justify-content: space-around;
            margin-top: 15px;
            padding-top: 10px;
            border-top: 1px solid #3a3b3c;
        }

        .boton-accion {
            cursor: pointer;
            color: #b0b3b8;
            font-weight: 500;
            transition: 0.2s;
        }

        .boton-accion:hover {
            color: #2d88ff;
        }

        .menu-lateral {
            background-color: #242526;
            border-left: 1px solid #3a3b3c;
            padding: 30px 20px;
        }

        .perfil {
            display: flex;
            align-items: center;
            margin-bottom: 30px;
        }

        .perfil .foto-usuario {
            width: 50px;
            height: 50px;
        }

        .lista-menu {
            list-style: none;
            padding: 0;
        }

        .lista-menu li {
            margin-bottom: 5px;
        }

        .lista-menu a {
            display: block;
            padding: 12px 10px;
            border-radius: 8px;
            text-decoration: none;
            color: #e4e6eb;
            transition: 0.2s;
        }

        .lista-menu a:hover {
            background-color: #3a3b3c;
            color: #2d88ff;
        }

        .lista-menu li:hover {
            background-color: #3a3b3c;
        }
    </style>
</head>

<body>

    <div class="pagina">
        <header class="barra-superior">
            <div class="logo"><a href="principal.php"><img src="awrorlogo.png" width="50px" height="50px"></a></div>

            <div class="tituloapp">
                AWROR
            </div>
        </header>
        <main class="contenido-principal">
            <div class="tarjeta-publicacion">
                <div class="cabecera-publicacion">
                    <div class="foto-usuario"></div>
                    <div>
                        <div class="nombre-usuario">nombre_usuario</div>
                    </div>
                </div>
                <div class="contenido-publicacion">
                    AAAAAAA
                </div>
                <div class="imagen-publicacion"></div>
                <div class="acciones">
                    <div class="boton-accion">Me gusta</div>
                    <div class="boton-accion">Comentar</div>
                    <div class="boton-accion">Compartir</div>
                </div>
            </div>
        </main>
        <aside class="menu-lateral">
            <div class="perfil">
                <div class="foto-usuario"></div>
                <div style="margin-left:10px;">
                    <strong>nombre_usuario</strong>
                </div>
            </div>
            <ul class="lista-menu">
                <li><a href="#">Perfil</a></li>
                <li><a href="#">Mascotas</a></li>
                <li><a href="#">Amigos</a></li>
                <li><a href="#">Historial de vacunas</a></li>
                <li><a href="#">Calendario</a></li>
                <li><a href="#">Consejos</a></li>
            </ul>
        </aside>
    </div>
</body>

</html>