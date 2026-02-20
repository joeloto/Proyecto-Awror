<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AWROR</title>

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

        header {
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


        .tituloapp a{
            color: #f7c775;
            font-weight: bold;
            font-size: 25px;
            text-decoration: none;
        }

        .tituloapp a:hover {
            color: white;
            font-weight: bold;
            font-size: 25px;
        }

        main {
            padding: 40px;
            display: flex;
            justify-content: center;
            overflow-y: auto;
        }

        .publicacion {
            background-color: #242526;
            width: 600px;
            height: 485px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
            padding: 20px;
            border: 1px solid #3a3b3c;
            /* display: none; */
        }

        .cabecera {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        .fotoperfil {
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

        .contenidop {
            margin: 15px 0;
            font-size: 15px;
            line-height: 1.5;
            color: #d0d2d6;
        }

        .imagen {
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
            color: #f7c775;
        }

        .opciones {
            background-color: #242526;
            border-left: 1px solid #3a3b3c;
            padding: 30px 20px;
        }

        .perfil {
            display: flex;
            align-items: center;
            margin-bottom: 30px;
        }

        .perfil .fotoperfil {
            width: 50px;
            height: 50px;
        }

        .menu {
            list-style: none;
            padding: 0;
        }

        .menu li {
            margin-bottom: 5px;
        }

        .menu a {
            display: block;
            padding: 12px 10px;
            border-radius: 8px;
            text-decoration: none;
            color: #e4e6eb;
            transition: 0.2s;
        }

        .menu a:hover {
            background-color: #3a3b3c;
            color: #f7c775;
        }

        .menu li:hover {
            background-color: #3a3b3c;
        }

        .botonpublicar {
            position: fixed;
            bottom: 30px;
            left: 30px;
            width: 100px;
            height: 60px;
            border-radius: 10px;
            border: none;
            background-color: #f7c775;
            color: white;
            font-size: 20px;
            font-weight: bold;
            cursor: pointer;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
            display: inline-block;
            line-height: 60px;
            text-align: center;
            vertical-align:middle;
            transition: background-color 0.3s, transform 0.2s;
        }

        .botonpublicar:hover {
            background-color: #f9d38c;
            transform: scale(1.1);
        }
    </style>
</head>

<body>

    <div class="pagina">
        <header>
            <div class="logo"><a href="./principal.php"><img src="../awrorlogo.png" width="50px" height="50px"></a></div>

            <div class="tituloapp">
                <a href="./principal.php">AWROR</a>
            </div>
        </header>
        <main>
            <div class="publicacion">
                <div class="cabecera">
                    <div class="fotoperfil"></div>
                    <div>
                        <div class="nombre-usuario">nombre_usuario</div>
                    </div>
                </div>
                <div class="contenidop">
                    AAAAAAA
                </div>
                <div class="imagen"></div>
                <div class="acciones">
                    <div class="boton-accion">Me gusta</div>
                    <div class="boton-accion">Comentar</div>
                    <div class="boton-accion">Compartir</div>
                </div>
            </div>
        </main>
        <aside class="opciones">
            <div class="perfil">
                <div class="fotoperfil"></div>
                <div style="margin-left:10px;">
                    <strong>nombre_usuario</strong>
                </div>
            </div>
            <ul class="menu">
                <li><a href="./perfil.php">Perfil</a></li>
                <li><a href="#">Mascotas</a></li>
                <li><a href="#">Historial de vacunas</a></li>
                <li><a href="#">Calendario</a></li>
            </ul>
        </aside>
    </div>
    <button class="botonpublicar">Publicar</button>
</body>

</html>