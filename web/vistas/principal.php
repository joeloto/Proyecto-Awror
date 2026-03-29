<?php
session_start();

if (!isset($_SESSION['id'])) {
    header("Location: iniciosesion.php");
    exit();
}

$usuario_id = $_SESSION['id'];
$api = "http://localhost:8080/apiawror/rest/posts";

if (isset($_POST['publicar'])) {

    $contenido = trim($_POST['contenido'] ?? '');
    $imagenBase64 = null;

    if (!empty($_FILES['imagen']['tmp_name'])) {
        $imagen = file_get_contents($_FILES['imagen']['tmp_name']);
        $imagenBase64 = base64_encode($imagen);
    }

    if ($contenido !== '' || $imagenBase64 !== null) {
        $data = [
            "user_id"   => $usuario_id,
            "contenido" => $contenido,
            "imagen"    => $imagenBase64
        ];

        $ch = curl_init($api);
        curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            'Content-Type: application/json'
        ]);
        curl_exec($ch);
        curl_close($ch);
    }

    header("Location: principal.php");
    exit();
}

if (isset($_POST['eliminar'])) {

    $post_id = $_POST['post_id'];

    $ch = curl_init($api . "/" . $post_id);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "DELETE");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_exec($ch);
    curl_close($ch);

    header("Location: principal.php");
    exit();
}

if (isset($_POST['editar'])) {
    $post_id = $_POST['post_id'];
    $contenido = trim($_POST['contenido_edit']);

    $data = [
        "user_id" => $usuario_id,
        "contenido" => $contenido
    ];

    $ch = curl_init($api . "/" . $post_id);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "PUT");
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    curl_exec($ch);
    curl_close($ch);

    header("Location: principal.php");
    exit();
}

$ch = curl_init($api);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$response = curl_exec($ch);
curl_close($ch);

$posts = json_decode($response, true);
if (!is_array($posts)) {
    $posts = [];
}
?>
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

        .tituloapp a:hover {
            color: white;
        }

        main {
            padding: 40px;
            display: flex;
            justify-content: center;
            overflow-y: auto;
            background-image: url('../fondoawror2.jpg');
        }

        .publicacion {
            background-color: white;
            width: 600px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
            padding: 20px;
            border: 1px solid #3a3b3c;
        }

        .cabecera {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
        }

        .nombre-usuario {
            font-weight: 600;
            color: black;
        }

        .contenidop {
            margin: 15px 0;
            font-size: 15px;
            line-height: 1.5;
            color: black;
            white-space: pre-wrap;
        }

        .imagen {
            width: 100%;
            max-height: 300px;
            object-fit: cover;
            border-radius: 10px;
            margin-top: 10px;
        }

        .acciones {
            display: flex;
            justify-content: flex-end;
            margin-top: 15px;
            padding-top: 10px;
            border-top: 1px solid #3a3b3c;
        }

        .boton-eliminar {
            background-color: #b02a37;
            color: white;
            border: none;
            padding: 8px 14px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: 0.2s;
        }

        .boton-eliminar:hover {
            background-color: #e03141;
            transform: scale(1.05);
        }

        .boton-editar {
            background-color: #0F748F;
            color: white;
            border: none;
            padding: 8px 14px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: 0.2s;
        }

        .boton-editar:hover {
            background-color: #1CBCE8;
            transform: scale(1.05);
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

        .botonpublicar {
            position: fixed;
            bottom: 30px;
            left: 30px;
            width: 100px;
            height: 60px;
            border-radius: 10px;
            border: none;
            background-color: #ea9d55;
            color: white;
            font-size: 20px;
            font-weight: bold;
            cursor: pointer;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
            transition: background-color 0.3s, transform 0.2s;
        }

        .botonpublicar:hover {
            background-color: #f7c760;
            transform: scale(1.1);
        }

        .btn-publicar {
            margin-top: 5px;
            padding: 10px 15px;
            border: none;
            border-radius: 8px;
            background: #ea9d55;
            cursor: pointer;
            color: white;
        }

        .btn-publicar:hover {
            background-color: #f7c760;
            transform: scale(1.1);
        }
    </style>
</head>

<script>
    function mostrarFormulario() {
        const form = document.getElementById("formPublicar");
        form.style.display = form.style.display === "none" ? "block" : "none";
    }

    function mostrarEditar(id) {
        const box = document.getElementById("editar" + id);
        box.style.display = box.style.display === "none" ? "block" : "none";
    }
</script>

<body>

    <div class="pagina">
        <header>
            <div class="logo"><a href="./principal.php"><img src="../awrorlogo.png"></a></div>
            <div class="tituloapp"><a href="./principal.php">AWROR</a></div>
        </header>

        <main>
            <div style="width:600px;">

                <div id="formPublicar" style="display:none; margin-bottom:20px;">
                    <form method="POST" enctype="multipart/form-data">
                        <textarea name="contenido" required
                            style="width:100%; height:100px; padding:10px; border-radius:8px; border:none; resize:vertical;"
                            placeholder="¿Qué quieres compartir hoy?"></textarea>

                        <input type="file" name="imagen" accept="image/*"
                            style="margin-top:10px; margin-bottom:10px; color:#e4e6eb;">

                        <button type="submit" name="publicar" class="btn-publicar">
                            <strong>Publicar</strong>
                        </button>
                    </form>
                </div>

                <?php if (!empty($posts)): ?>
                    <?php foreach ($posts as $post): ?>
                        <div class="publicacion" style="margin-bottom:20px;">
                            <div class="cabecera">
                                <div class="nombre-usuario">
                                    <?= htmlspecialchars($post['username'] ?? $_SESSION['user_name']) ?>
                                </div>
                            </div>

                            <?php if (!empty($post['contenido'])): ?>
                                <div class="contenidop"><?= nl2br(htmlspecialchars($post['contenido'])) ?></div>
                            <?php endif; ?>

                            <?php if (!empty($post['imagen'])): ?>
                                <img src="data:image/jpeg;base64,<?= $post['imagen'] ?>" class="imagen">
                            <?php endif; ?>

                            <div class="acciones">

                                <?php if ($post['user_id'] == $usuario_id): ?>
                                    <button type="button" class="boton-editar" onclick="mostrarEditar(<?= $post['id'] ?>)">Editar</button>

                                    <form method="POST" style="margin-left:10px;"
                                        onsubmit="return confirm('¿Seguro que quieres eliminar esta publicación?');">
                                        <input type="hidden" name="post_id" value="<?= $post['id'] ?>">
                                        <button type="submit" name="eliminar" class="boton-eliminar">Eliminar</button>
                                    </form>
                                <?php endif; ?>

                            </div>

                            <?php if ($post['user_id'] == $usuario_id): ?>
                                <div id="editar<?= $post['id'] ?>" style="display:none; margin-top:10px;">
                                    <form method="POST">
                                        <textarea name="contenido_edit" style="width:100%; height:80px; border-radius:8px; padding:10px;"><?= htmlspecialchars($post['contenido']) ?></textarea>
                                        <input type="hidden" name="post_id" value="<?= $post['id'] ?>">
                                        <button type="submit" name="editar" class="boton-eliminar" style="background:#f7c775; color:black; margin-top:5px;">Guardar cambios</button>
                                    </form>
                                </div>
                            <?php endif; ?>


                            <div id="editar<?= $post['id'] ?>" style="display:none; margin-top:10px;">
                                <form method="POST">
                                    <textarea name="contenido_edit" style="width:100%; height:80px; border-radius:8px; padding:10px;"><?= htmlspecialchars($post['contenido']) ?></textarea>
                                    <input type="hidden" name="post_id" value="<?= $post['id'] ?>">
                                    <button type="submit" name="editar" class="boton-eliminar" style="background:#f7c775; color:black; margin-top:5px;">Guardar cambios</button>
                                </form>
                            </div>

                        </div>
                    <?php endforeach; ?>
                <?php else: ?>
                    <p style="text-align:center; color:#b0b3b8;">No hay publicaciones todavía.</p>
                <?php endif; ?>

            </div>
        </main>

        <aside class="opciones">
            <div class="perfil">
                <div style="margin-left:10px;">
                    <strong><?= htmlspecialchars($_SESSION['user_name']) ?></strong>
                </div>
            </div>

            <ul class="menu">
                <li><a href="./perfil.php">Perfil</a></li>
                <li><a href="./mascotas.php">Mascotas</a></li>
                <li><a href="./vacunas.php">Historial de vacunas</a></li>
            </ul>
        </aside>
    </div>

    <button class="botonpublicar" onclick="mostrarFormulario()">Publicar</button>

</body>

</html>