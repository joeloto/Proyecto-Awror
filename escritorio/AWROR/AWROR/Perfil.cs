using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Collections.Specialized.BitVector32;
using System.Net.Http;
using System.Text;
using Newtonsoft.Json;


namespace AWROR
{
    public partial class Perfil : Form
    {
        public Perfil()
        {
            InitializeComponent();
            controlesPerfil = new List<Control>()
            {
                txtNombre,
                txtApellido,
                txtNombreUsuario,
                txtEmail,
                txtPassword,
                btnActualizar,
                btnBorrar,
                btnCerrarSesion
            };

            btnCerrarSesion.Anchor = AnchorStyles.Bottom | AnchorStyles.Left;
            this.WindowState = FormWindowState.Maximized;
            lblUsuario.Text = Sesion.UserName;
            txtNombre.Text = Sesion.RealName;
            txtApellido.Text = Sesion.RealSurname;
            txtNombreUsuario.Text = Sesion.UserName;
            txtEmail.Text = Sesion.Email;
            CentrarControles();
            AjustarTamanos();
            this.Resize += (s, e) =>
            {
                CentrarControles();
                AjustarTamanos();
            };

        }

        private async void btnActualizar_Click(object sender, EventArgs e)
        {
            var datos = new
            {
                user_name = txtNombreUsuario.Text,
                real_name = txtNombre.Text,
                real_surname = txtApellido.Text,
                email = txtEmail.Text,
                password = txtPassword.Text
            };

            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");

                    string json = JsonConvert.SerializeObject(datos);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    HttpResponseMessage response =
                        await client.PutAsync($"users/{Sesion.UserId}", content);

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Perfil actualizado correctamente");

                        Sesion.UserName = txtNombreUsuario.Text;
                        Sesion.RealName = txtNombre.Text;
                        Sesion.RealSurname = txtApellido.Text;
                        Sesion.Email = txtEmail.Text;

                        lblUsuario.Text = Sesion.UserName;
                    }
                    else
                    {
                        MessageBox.Show("Error al actualizar el perfil");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión: " + ex.Message);
            }
        }

        private async void btnBorrar_Click(object sender, EventArgs e)
        {
            var confirm = MessageBox.Show(
                "¿Seguro que quieres borrar tu cuenta? Esta acción no se puede deshacer.",
                "Confirmar",
                MessageBoxButtons.YesNo,
                MessageBoxIcon.Warning
            );

            if (confirm != DialogResult.Yes)
            {
                return;
            }

            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");

                    HttpResponseMessage response =
                        await client.DeleteAsync($"users/{Sesion.UserId}");

                    if (response.IsSuccessStatusCode)
                    {
                        MessageBox.Show("Cuenta eliminada correctamente");
                        IniciarSesion i = new IniciarSesion();
                        i.Show();
                        this.Close();
                    }
                    else
                    {
                        MessageBox.Show("Error al eliminar la cuenta");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión: " + ex.Message);
            }
        }

        private void btnCerrarSesion_Click(object sender, EventArgs e)
        {
            Sesion.UserId = 0;
            Sesion.UserName = "";
            Sesion.RealName = "";
            Sesion.RealSurname = "";
            Sesion.Email = "";

            IniciarSesion login = new IniciarSesion();
            login.Show();
            this.Close();
        }

        private void btnPerfil_Click(object sender, EventArgs e)
        {
            Perfil perfil = new Perfil();
            perfil.Show();
            this.Hide();
        }

        private void btnMascotas_Click(object sender, EventArgs e)
        {
            Mascotas mascotas = new Mascotas();
            mascotas.Show();
            this.Hide();
        }

        private void btnVacunas_Click(object sender, EventArgs e)
        {
            Vacunas vacunas = new Vacunas();
            vacunas.Show();
            this.Hide();
        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {
            Principal p = new Principal();
            p.Show();
            this.Hide();
        }

        private List<Control> controlesPerfil;
        private void CentrarControles()
        {
            int anchoFormulario = this.ClientSize.Width;
            int anchoPanelLateral = panel2.Width;

            int anchoDisponible = anchoFormulario - anchoPanelLateral;
            int centroDisponible = anchoDisponible / 2;

            int inicioY = 120; 
            int separacion = 15;

            foreach (var c in controlesPerfil)
            {
                c.Left = centroDisponible - (c.Width / 2);
                c.Top = inicioY;

                inicioY += c.Height + separacion;
            }
        }

        private void AjustarTamanos()
        {
            foreach (var c in controlesPerfil)
            {
                if (c is Button)
                {
                    c.Width = 200;
                    c.Height = 40;
                }
                else
                {
                    c.Width = 350;
                    c.Height = 40;
                }
            }
        }





    }
}
