<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil</title>

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

        .main {
            padding: 40px;
            display: flex;
            justify-content: center;
            overflow-y: auto;
        }

        .formulario {
            background-color: #242526;
            width: 600px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
            padding: 20px;
            border: 1px solid #3a3b3c;
            height: 660px;
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

        .datos {
            margin: 15px 0;
            font-size: 15px;
            line-height: 1.5;
            color: #d0d2d6;
        }

        .acciones {
            display: flex;
            justify-content: space-around;
            margin-top: 15px;
            padding-top: 10px;
            border-top: 1px solid #3a3b3c;
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

        .perfil .foto-usuario {
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

        .boton-guardar {
            background-color: #f7c775;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
            transition: background-color 0.3s;
            font-size: 20px;
            font-weight: bold;
            margin-top: 15px;
        }

        .boton-guardar:hover {
            background-color: #ea9d55;
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
        <main class="main">
            <div class="formulario">

                <h2 style="margin-bottom:20px; color:#f7c775;">Datos del Perfil</h2>

                <form action="../controladores/controlador_actualizar.php" method="POST">

                    <div class="datos">
                        <label for="real_name">Nombre:</label><br><br>
                        <input type="text" id="real_name" name="real_name"
                            style="width:100%; padding:10px; border-radius:8px; border:none;">
                    </div>
                    <div class="datos">
                        <label for="real_surname">Primer apellido:</label><br><br>
                        <input type="text" id="real_surname" name="real_surname"
                            style="width:100%; padding:10px; border-radius:8px; border:none;">
                    </div>
                    <div class="datos">
                        <label for="email">Correo electrónico:</label><br><br>
                        <input type="email" id="email" name="email"
                            style="width:100%; padding:10px; border-radius:8px; border:none;">
                    </div>
                    <div class="datos">
                        <label for="user_name">Nombre de usuario:</label><br><br>
                        <input type="text" id="user_name" name="user_name"
                            style="width:100%; padding:10px; border-radius:8px; border:none;">
                    </div>
                    <div class="datos">
                        <label for="password">Contraseña:</label><br><br>
                        <input type="password" id="password" name="password"
                            style="width:100%; padding:10px; border-radius:8px; border:none;">
                    </div>
                    <div class="acciones">
                        <button type="submit" class="boton-guardar">
                            Guardar cambios
                        </button>
                    </div>

                </form>

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
</body>

</html>