<?php
session_start();

if (!isset($_SESSION['id'])) {
    header("Location: ../iniciosesion.php");
    exit();
}

$usuario_id = $_SESSION['id'];
$apiPets = "http://localhost:8080/apiawror/rest/pets/user/" . $usuario_id;
$apiVaccines = "http://localhost:8080/apiawror/rest/vaccines";

$ch = curl_init($apiPets);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$petsResponse = curl_exec($ch);
curl_close($ch);

$pets = json_decode($petsResponse, true);
if (!is_array($pets)) $pets = [];

$selectedPet = $_GET['pet'] ?? (count($pets) > 0 ? $pets[0]['id'] : null);

$vaccines = [];
if ($selectedPet) {
    $ch = curl_init($apiVaccines . "/pet/" . $selectedPet);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    $vaccinesResponse = curl_exec($ch);
    curl_close($ch);
    $vaccines = json_decode($vaccinesResponse, true);
    if (!is_array($vaccines)) $vaccines = [];
}

if (isset($_POST['crear'])) {
    $data = [
        "petId" => intval($_POST['pet_id']),
        "name" => trim($_POST['name']),
        "dateGiven" => trim($_POST['date_given']),
        "nextDate" => trim($_POST['next_date']),
        "notes" => trim($_POST['notes'])
    ];

    $ch = curl_init($apiVaccines);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    curl_exec($ch);
    curl_close($ch);

    header("Location: vacunas.php?pet=" . $_POST['pet_id']);
    exit();
}

if (isset($_POST['editar'])) {
    $id = $_POST['vaccine_id'];

    $data = [
        "name" => trim($_POST['name_edit']),
        "dateGiven" => trim($_POST['date_given_edit']),
        "nextDate" => trim($_POST['next_date_edit']),
        "notes" => trim($_POST['notes_edit'])
    ];

    $ch = curl_init($apiVaccines . "/" . $id);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "PUT");
    curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
    curl_exec($ch);
    curl_close($ch);

    header("Location: vacunas.php?pet=" . $_POST['pet_id']);
    exit();
}

if (isset($_POST['eliminar'])) {
    $id = $_POST['vaccine_id'];

    $ch = curl_init($apiVaccines . "/" . $id);
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "DELETE");
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_exec($ch);
    curl_close($ch);

    header("Location: vacunas.php?pet=" . $_POST['pet_id']);
    exit();
}
?>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vacunas - AWROR</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI';
        }

        body {
            background: #18191a;
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
            background: #242526;
            padding: 0 40px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            border-bottom: 1px solid #3a3b3c;
        }

        header img {
            width: 50px;
            border-radius: 8px;
        }

        header a {
            color: #f7c775;
            font-size: 25px;
            font-weight: bold;
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

        .contenido {
            width: 800px;
        }

        .selector {
            margin-bottom: 20px;
        }

        .selector select {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: none;
        }

        .lista {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .card {
            background: white;
            border-radius: 12px;
            border: 1px solid #3a3b3c;
            padding: 15px;
        }

        .card h3 {
            margin-bottom: 5px;
            color: black;
        }

        .card p {
            margin: 3px 0;
            color: gray;
        }

        .card-actions {
            margin-top: 10px;
            display: flex;
            gap: 10px;
        }

        button {
            cursor: pointer;
            border: none;
            border-radius: 8px;
            padding: 8px 12px;
            font-weight: bold;
        }

        .btn-editar {
            background-color: #0F748F;
            color: white;
        }

        .btn-editar:hover {
            background-color: #1CBCE8;
            transform: scale(1.05);
        }

        .btn-borrar {
            background: #b02a37;
            color: white;
        }

        .btn-borrar:hover {
            background-color: #e03141;
            transform: scale(1.05);
        }

        .form-box {
            background: #242526;
            padding: 20px;
            border-radius: 12px;
            border: 1px solid #3a3b3c;
            margin-bottom: 20px;
        }

        .form-box input,
        .form-box textarea {
            width: 100%;
            padding: 10px;
            border-radius: 8px;
            border: none;
            margin-bottom: 10px;
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

        .botonvacuna {
            position: fixed;
            bottom: 30px;
            left: 30px;
            width: 160px;
            height: 60px;
            border-radius: 10px;
            border: none;
            background: #f7c775;
            color: white;
            font-size: 18px;
            font-weight: bold;
            cursor: pointer;
        }
    </style>
    <script>
        function toggleForm() {
            const f = document.getElementById("formVacuna");
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

                <div class="selector">
                    <form method="GET">
                        <select name="pet" onchange="this.form.submit()">
                            <?php foreach ($pets as $p): ?>
                                <option value="<?= $p['id'] ?>" <?= $selectedPet == $p['id'] ? 'selected' : '' ?>>
                                    <?= htmlspecialchars($p['name']) ?>
                                </option>
                            <?php endforeach; ?>
                        </select>
                    </form>
                </div>

                <?php if ($selectedPet): ?>
                    <div id="formVacuna" class="form-box" style="display:none;">
                        <form method="POST">
                            <input type="hidden" name="pet_id" value="<?= $selectedPet ?>">
                            <input type="text" name="name" placeholder="Nombre de la vacuna" required>
                            <input type="date" name="date_given" required>
                            <input type="date" name="next_date">
                            <textarea name="notes" placeholder="Notas"></textarea>
                            <button type="submit" name="crear" class="btn-editar" style="width:100%;">Añadir vacuna</button>
                        </form>
                    </div>

                    <div class="lista">
                        <?php foreach ($vaccines as $v): ?>
                            <div class="card">
                                <h3><?= htmlspecialchars($v['name']) ?></h3>
                                <p>Fecha aplicada: <?= htmlspecialchars($v['dateGiven']) ?></p>
                                <p>Próxima dosis: <?= htmlspecialchars($v['nextDate']) ?></p>
                                <p><?= htmlspecialchars($v['notes']) ?></p>

                                <div class="card-actions">
                                    <button class="btn-editar" onclick="toggleEdit(<?= $v['id'] ?>)">Editar</button>

                                    <form method="POST">
                                        <input type="hidden" name="vaccine_id" value="<?= $v['id'] ?>">
                                        <input type="hidden" name="pet_id" value="<?= $selectedPet ?>">
                                        <button type="submit" name="eliminar" class="btn-borrar">Eliminar</button>
                                    </form>
                                </div>

                                <div id="edit<?= $v['id'] ?>" style="display:none; margin-top:10px;">
                                    <form method="POST">
                                        <input type="hidden" name="vaccine_id" value="<?= $v['id'] ?>">
                                        <input type="hidden" name="pet_id" value="<?= $selectedPet ?>">
                                        <input type="text" name="name_edit" value="<?= htmlspecialchars($v['name']) ?>" required>
                                        <input type="date" name="date_given_edit" value="<?= htmlspecialchars($v['dateGiven']) ?>" required>
                                        <input type="date" name="next_date_edit" value="<?= htmlspecialchars($v['nextDate']) ?>">
                                        <textarea name="notes_edit"><?= htmlspecialchars($v['notes']) ?></textarea>
                                        <button type="submit" name="editar" class="btn-editar" style="width:100%;">Guardar cambios</button>
                                    </form>
                                </div>
                            </div>
                        <?php endforeach; ?>

                        <?php if (empty($vaccines)): ?>
                            <p style="text-align:center; color:#b0b3b8;">Esta mascota no tiene vacunas registradas.</p>
                        <?php endif; ?>
                    </div>
                <?php else: ?>
                    <p style="text-align:center; color:#b0b3b8;">Primero añade una mascota para gestionar sus vacunas.</p>
                <?php endif; ?>

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

    <?php if ($selectedPet): ?>
        <button class="botonvacuna" onclick="toggleForm()">Añadir vacuna</button>
    <?php endif; ?>

</body>

</html>