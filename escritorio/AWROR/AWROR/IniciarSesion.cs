using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using static System.Collections.Specialized.BitVector32;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;


namespace AWROR
{
    public partial class IniciarSesion : Form
    {
        public IniciarSesion()
        {
            InitializeComponent();
        }

        private void txtEmail_Enter(object sender, EventArgs e)
        {
            txtEmail.Text = "";
        }

        private void txtEmail_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtEmail.Text))
            {
                txtEmail.Text = "Correo electrónico";
            }
        }

        private void txtPassword_Enter(object sender, EventArgs e)
        {
            txtPassword.Text = "";
            txtPassword.PasswordChar = '*';
        }

        private void txtPassword_Leave(object sender, EventArgs e)
        {
            if (string.IsNullOrWhiteSpace(txtPassword.Text))
            {
                txtPassword.Text = "Contraseña";
                txtPassword.PasswordChar = '\0';

            }
        }

        private void btnCrearCuenta_Click(object sender, EventArgs e)
        {
            CrearCuenta f = new CrearCuenta();
            f.Show();
            this.Hide();
        }

        private async void btnInicioSesion_Click(object sender, EventArgs e)
        {
            if (txtEmail.Text == "Correo electrónico" || txtPassword.Text == "Contraseña")
            {
                MessageBox.Show("Por favor, introduce tu correo y contraseña.");
                return;
            }

            var loginData = new
            {
                email = txtEmail.Text,
                password = txtPassword.Text
            };

            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");

                    string json = JsonConvert.SerializeObject(loginData);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    HttpResponseMessage response = await client.PostAsync("users/login", content);

                    string result = await response.Content.ReadAsStringAsync();

                    if (response.IsSuccessStatusCode)
                    {
                        dynamic user = JsonConvert.DeserializeObject(result);

                        Sesion.UserId = user.id;
                        Sesion.UserName = user.user_name;
                        Sesion.RealName = user.real_name;
                        Sesion.RealSurname = user.real_surname;
                        Sesion.Email = user.email;

                        MessageBox.Show("Bienvenido " + Sesion.UserName);
                        
                        Principal p = new Principal();
                        p.Show();
                        this.Hide();
                    }
                    else
                    {
                        MessageBox.Show("Correo o contraseña incorrectos.");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión con el servidor: " + ex.Message);
            }
        }

    }
}
