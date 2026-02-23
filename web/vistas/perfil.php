<?php
session_start();

if (!isset($_SESSION['id'])) {
    header("Location: ../iniciosesion.php");
    exit();
}

$mensaje = '';
$id = $_SESSION['id'];

$ch = curl_init("http://localhost:8080/apiawror/rest/users/$id");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$response = curl_exec($ch);
$status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

if ($status == 200) {
    $usuario = json_decode($response, true);
} else {
    session_destroy();
    header("Location: ../iniciosesion.php");
    exit();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['guardar'])) {

    $update_data = [
        'user_name'   => $_POST['user_name'],
        'real_name'   => $_POST['real_name'],
        'real_surname' => $_POST['real_surname'],
        'email'       => $_POST['email'],
        'password'    => $_POST['password'] 
    ];

    $ch = curl_init("http://localhost:8080/apiawror/rest/users/$id");
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "PUT");
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($update_data));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);

    $response = curl_exec($ch);
    $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($status == 200) {
        $mensaje = "Datos actualizados correctamente.";

        $_SESSION['user_name'] = $_POST['user_name'];
        $_SESSION['real_name'] = $_POST['real_name'];
        $_SESSION['real_surname'] = $_POST['real_surname'];
        $_SESSION['email'] = $_POST['email'];

        header("Location: perfil.php");
        exit();
    } else {
        $mensaje = "Error al actualizar.";
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['borrar'])) {

    $ch = curl_init("http://localhost:8080/apiawror/rest/users/$id");
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "DELETE");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    curl_exec($ch);
    $status = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);

    if ($status == 200) {
        session_destroy();
        header("Location: ../iniciosesion.php");
        exit();
    } else {
        $mensaje = "Error al borrar cuenta.";
    }
}
?>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Perfil - AWROR</title>
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
            grid-column: 1/3;
            background-color: #242526;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 40px;
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

        .tituloapp a {
            color: #f7c775;
            font-weight: bold;
            font-size: 25px;
            text-decoration: none;
        }

        .tituloapp a:hover{
            color: white;
        }

        .main {
            padding: 40px;
            display: flex;
            justify-content: center;
        }

        .formulario {
            background-color: #242526;
            width: 600px;
            border-radius: 12px;
            padding: 20px;
            border: 1px solid #3a3b3c;
        }

        .datos {
            margin: 15px 0;
        }

        input {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: none;
        }

        .acciones {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        .boton-guardar {
            background-color: #f7c775;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 18px;
            font-weight: bold;
            margin-top: 10px;
        }

        .boton-guardar:hover {
            background-color: #ea9d55;
        }

        .boton-borrar {
            background-color: #b02a37;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 18px;
            margin-top: 15px;
        }

        .boton-borrar:hover {
            background-color: #772222;
        }

        .mensaje {
            color: green;
            font-weight: bold;
            margin-top: 15px;
            text-align: center;
        }

        .fotoperfil {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            background-color: #3a3b3c;
            margin-right: 12px;
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
    </style>
</head>

<body>

    <div class="pagina">
        <header>
            <div class="logo"><a href="./principal.php"><img src="../awrorlogo.png"></a></div>
            <div class="tituloapp"><a href="./principal.php">AWROR</a></div>
        </header>

        <main class="main">
            <div class="formulario">
                <h2 style="margin-bottom:20px; color:#f7c775;">Datos del Perfil</h2>
                <?php if ($mensaje): ?><div class="mensaje"><?= htmlspecialchars($mensaje) ?></div><?php endif; ?>
                <form method="POST">
                    <div class="datos"><label>Nombre:</label><br><br><input type="text" name="real_name" value="<?= htmlspecialchars($usuario['real_name']) ?>"></div>
                    <div class="datos"><label>Primer apellido:</label><br><br><input type="text" name="real_surname" value="<?= htmlspecialchars($usuario['real_surname']) ?>"></div>
                    <div class="datos"><label>Correo electrónico:</label><br><br><input type="email" name="email" value="<?= htmlspecialchars($usuario['email']) ?>"></div>
                    <div class="datos"><label>Nombre de usuario:</label><br><br><input type="text" name="user_name" value="<?= htmlspecialchars($usuario['user_name']) ?>"></div>
                    <div class="datos"><label>Contraseña:</label><br><br><input type="password" name="password" placeholder="Nueva contraseña"></div>
                    <div class="acciones">
                        <button type="submit" name="guardar" class="boton-guardar">Guardar cambios</button>
                        <button type="submit" name="borrar"
                            onclick="return confirm('¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.')"
                            class="boton-borrar">Borrar cuenta</button>
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