<?php
session_start();

if (!isset($_SESSION['id'])) {
    header("Location: ../iniciosesion.php");
    exit();
}

$usuario_id = $_SESSION['id'];
$api = "http://localhost:8080/apiawror/rest/pets";

if (isset($_POST['crear'])) {
    $name = trim($_POST['name']);
    $type = trim($_POST['type']);
    $imageBase64 = null;

    if (!empty($_FILES['image']['tmp_name'])) {
        $img = file_get_contents($_FILES['image']['tmp_name']);
        $imageBase64 = base64_encode($img);
    }

    $data = [
        "user_id" => $usuario_id,
        "name" => $name,
        "type" => $type,
        "image" => $imageBase64
    ];

    $ch = curl_init($api);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    curl_exec($ch);
    curl_close($ch);

    header("Location: mascotas.php");
    exit();
}

if (isset($_POST['editar'])) {
    $pet_id = $_POST['pet_id'];
    $name = trim($_POST['name_edit']);
    $type = trim($_POST['type_edit']);
    $imageBase64 = null;

    if (!empty($_FILES['image_edit']['tmp_name'])) {
        $img = file_get_contents($_FILES['image_edit']['tmp_name']);
        $imageBase64 = base64_encode($img);
    } else {
        $imageBase64 = $_POST['image_old'] !== '' ? $_POST['image_old'] : null;
    }

    $data = [
        "name" => $name,
        "type" => $type,
        "image" => $imageBase64
    ];

    $ch = curl_init($api . "/" . $pet_id);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "PUT");
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    curl_exec($ch);
    curl_close($ch);

    header("Location: mascotas.php");
    exit();
}

if (isset($_POST['eliminar'])) {
    $pet_id = $_POST['pet_id'];

    $ch = curl_init($api . "/" . $pet_id);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "DELETE");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_exec($ch);
    curl_close($ch);

    header("Location: mascotas.php");
    exit();
}

$ch = curl_init($api . "/user/" . $usuario_id);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$response = curl_exec($ch);
curl_close($ch);

$pets = json_decode($response, true);
if (!is_array($pets)) $pets = [];
?>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mascotas - AWROR</title>
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
            background-image: url('../fondoawror2.jpg');
        }

        header {
            grid-column: 1/3;
            background: #242526;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 40px;
            border-bottom: 1px solid #3a3b3c;
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
        }

        .contenido {
            width: 800px;
        }

        .form-box {
            background: #242526;
            padding: 20px;
            border-radius: 12px;
            border: 1px solid #3a3b3c;
            margin-bottom: 20px;
        }

        .form-box input {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: none;
            margin-bottom: 10px;
        }

        .form-box button {
            width: 100%;
            padding: 10px;
            border: none;
            border-radius: 8px;
            background: #f7c775;
            font-weight: bold;
            cursor: pointer;
        }

        .lista-mascotas {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .card {
            background: white;
            border-radius: 12px;
            border: 1px solid #3a3b3c;
            padding: 15px;
            display: flex;
            align-items: center;
        }

        .card img {
            width: 120px;
            height: 120px;
            object-fit: cover;
            border-radius: 10px;
            margin-right: 15px;
        }

        .card-info {
            flex: 1;
        }

        .card-info h3 {
            margin-bottom: 5px;
            color: black;
        }

        .card-info p {
            margin: 0;
            color: black;
        }

        .card-actions {
            margin-left: 10px;
            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        .btn-edit {
            background-color: #0F748F;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 6px 10px;
            cursor: pointer;
            font-weight: bold;
        }

        .btn-edit:hover {
            background-color: #1CBCE8;
            transform: scale(1.05);
        }

        .btn-borrar {
            background: #b02a37;
            color: white;
            border: none;
            border-radius: 8px;
            padding: 6px 10px;
            cursor: pointer;
            font-weight: bold;
        }

        .btn-borrar:hover {
            background-color: #e03141;
            transform: scale(1.05);
        }

        .opciones {
            background: #242526;
            border-left: 1px solid #3a3b3c;
            padding: 30px 20px;
        }

        .perfil {
            display: flex;
            align-items: center;
            margin-bottom: 30px;
        }

        .perfil div {
            margin-left: 10px;
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
        }

        .menu a:hover {
            background: #3a3b3c;
            color: #f7c775;
        }

        .botonmascota {
            position: fixed;
            bottom: 30px;
            left: 30px;
            width: 130px;
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

        .botonmascota:hover {
            background-color: #f7c760;
            transform: scale(1.1);
        }
    </style>
    <script>
        function toggleForm() {
            const f = document.getElementById("formMascota");
            f.style.display = f.style.display === "none" ? "block" : "none";
        }

        function toggleEdit(id) {
            const box = document.getElementById("edit" + id);
            box.style.display = box.style.display === "none" ? "block" : "none";
        }
    </script>
</head>

<body>
    <div class="pagina">
        <header>
            <div class="logo"><a href="./principal.php"><img src="../awrorlogo.png"></a></div>
            <div class="tituloapp"><a href="./principal.php">AWROR</a></div>
        </header>

        <main>
            <div class="contenido">
                <div id="formMascota" style="display:none;">
                    <form method="POST" enctype="multipart/form-data">
                        <input type="text" name="name" placeholder="Nombre de la mascota" required>
                        <input type="text" name="type" placeholder="Tipo de animal (Perro, Gato...)" required>
                        <input type="file" name="image" accept="image/*">
                        <button type="submit" name="crear">Añadir mascota</button>
                    </form>
                </div>

                <div class="lista-mascotas">
                    <?php foreach ($pets as $pet): ?>
                        <div class="card">
                            <?php if (!empty($pet['image'])): ?>
                                <img src="data:image/jpeg;base64,<?= $pet['image'] ?>">
                            <?php else: ?>
                                <img src="https://via.placeholder.com/120x120?text=Mascota">
                            <?php endif; ?>

                            <div class="card-info">
                                <h3><?= htmlspecialchars($pet['name']) ?></h3>
                                <p><?= htmlspecialchars($pet['type']) ?></p>

                                <div id="edit<?= $pet['id'] ?>" style="display:none; margin-top:10px;">
                                    <div style="display:flex; gap:15px; align-items:flex-start;">
                                        <img src="<?= !empty($pet['image']) ? 'data:image/jpeg;base64,' . $pet['image'] : 'https://via.placeholder.com/120x120?text=Mascota' ?>"
                                            style="width:120px; height:120px; object-fit:cover; border-radius:10px;">

                                        <form method="POST" enctype="multipart/form-data" style="flex:1;">
                                            <input type="text" name="name_edit" value="<?= htmlspecialchars($pet['name']) ?>" required>
                                            <input type="text" name="type_edit" value="<?= htmlspecialchars($pet['type']) ?>" required>
                                            <div style="font-size:12px; color:#b0b3b8; margin-bottom:5px;">
                                                Imagen actual guardada. Si quieres cambiarla, selecciona una nueva.
                                            </div>
                                            <input type="file" name="image_edit" accept="image/*">
                                            <input type="hidden" name="image_old" value="<?= htmlspecialchars($pet['image']) ?>">
                                            <input type="hidden" name="pet_id" value="<?= $pet['id'] ?>">
                                            <button type="submit" name="editar" class="btn-edit" style="margin-top:5px; width:100%;">Guardar cambios</button>
                                        </form>
                                    </div>
                                </div>
                            </div>

                            <div class="card-actions">
                                <button class="btn-edit" onclick="toggleEdit(<?= $pet['id'] ?>)">Editar</button>
                                <form method="POST">
                                    <input type="hidden" name="pet_id" value="<?= $pet['id'] ?>">
                                    <button type="submit" name="eliminar" class="btn-borrar">Eliminar</button>
                                </form>
                            </div>
                        </div>
                    <?php endforeach; ?>

                    <?php if (empty($pets)): ?>
                        <p style="text-align:center; color:#b0b3b8;">Todavía no has añadido ninguna mascota.</p>
                    <?php endif; ?>
                </div>
            </div>
        </main>

        <aside class="opciones">
            <div class="perfil">
                <div><strong><?= htmlspecialchars($_SESSION['user_name']) ?></strong></div>
            </div>
            <ul class="menu">
                <li><a href="./perfil.php">Perfil</a></li>
                <li><a href="./mascotas.php">Mascotas</a></li>
                <li><a href="./vacunas.php">Historial de vacunas</a></li>
            </ul>
        </aside>
    </div>

    <button class="botonmascota" onclick="toggleForm()">Añadir mascota</button>
</body>

</html>