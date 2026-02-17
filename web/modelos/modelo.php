<?php
class User {

    private $user;
    private $db;

    public function __construct()
    {
        $this->user = array();
        $this->db = new PDO('mysql:host=localhost;dbname=awror;charset=utf8', 'root', '');
    }

    public function setUsuario($nombre, $apellido, $email, $password, $username){
        $sql = "INSERT INTO users (real_name, real_surname, email, password, user_name)
                VALUES ('$nombre','$apellido','$email','$password','$username')";
        $result = $this->db->query($sql);
        $this->db = null;
        return $result;
    }

    public function getUsers(){
        $sql = "SELECT * FROM users";
        $result = $this->db->query($sql);
        $this->user = $result->fetchAll(PDO::FETCH_ASSOC);
        $this->db = null;
        return $this->user;
    }

    public function borrar($id){
        $sql = "DELETE FROM users WHERE id={$id} LIMIT 1";
        $result = $this->db->query($sql);
        $this->db = null;
        return $result;
    }

    public function editar($id){
        $sql = "SELECT nombre,artista,ano,genero,imagen,id FROM albumes WHERE id={$id}";
        $result = $this->db->query($sql);
        $dato = $result->fetchAll(PDO::FETCH_ASSOC);
        $this->db = null;
        return $dato;
    }

    public function actualizar($id, $nombre, $artista, $ano, $genero, $imagen){
        $sql = "UPDATE albumes SET nombre='$nombre', artista='$artista', ano='$ano', genero='$genero',imagen='$imagen' WHERE id={$id}";
        $result = $this->db->query($sql);
        $this->db = null;
        return $result;
    }
}
?>
