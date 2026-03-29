using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace AWROR
{
    public partial class Vacunas : Form
    {
        public Vacunas()
        {
            InitializeComponent();
            btnAgregarVacuna.Anchor = AnchorStyles.Bottom | AnchorStyles.Right;
            this.WindowState = FormWindowState.Maximized;
            lblUsuario.Text = Sesion.UserName;
            cmbMascotas.Location = new Point(
                flowLayoutPanelVacunas.Right - cmbMascotas.Width,
                flowLayoutPanelVacunas.Top - cmbMascotas.Height - 5
            );


            this.Load += async (s, e) =>
            {
                await CargarMascotas();

                cmbMascotas.SelectedIndexChanged += async (s2, e2) =>
                {
                    if (cmbMascotas.SelectedValue is int petId)
                    {
                        await CargarVacunasDeMascota(petId);
                    }
                };

                if (cmbMascotas.SelectedValue is int firstPetId)
                {
                    await CargarVacunasDeMascota(firstPetId);
                }
            };
        }

        private void btnPerfil_Click(object sender, EventArgs e)
        {
            new Perfil().Show();
            this.Hide();
        }

        private void btnMascotas_Click(object sender, EventArgs e)
        {
            new Mascotas().Show();
            this.Hide();
        }

        private void btnVacunas_Click(object sender, EventArgs e)
        {
            new Vacunas().Show();
            this.Hide();
        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {
            new Principal().Show();
            this.Hide();
        }

        private async void btnAgregarVacuna_Click(object sender, EventArgs e)
        {
            if (cmbMascotas.SelectedValue is int petId)
            {
                VacunaDialog dialog = new VacunaDialog();

                if (dialog.ShowDialog() == DialogResult.OK)
                {
                    await SubirVacuna(
                        petId,
                        dialog.Nombre,
                        dialog.FechaAdministrada,
                        dialog.FechaProxima,
                        dialog.Notes
                    );
                }
            }
            else
            {
                MessageBox.Show("Selecciona una mascota primero");
                return;
            }
        }

        private async Task SubirVacuna(int petId, string nombre, DateTime fechaAdmin, DateTime fechaProx, string notes)
        {
            var vacuna = new
            {
                petId = petId,
                name = nombre,
                dateGiven = fechaAdmin.ToString("yyyy-MM-dd"),
                nextDate = fechaProx.ToString("yyyy-MM-dd"),
                notes = notes
            };

            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");

                    string json = Newtonsoft.Json.JsonConvert.SerializeObject(vacuna);
                    var content = new StringContent(json, Encoding.UTF8, "application/json");

                    HttpResponseMessage response = await client.PostAsync("vaccines", content);

                    if (response.IsSuccessStatusCode)
                    {
                        await CargarVacunasDeMascota(petId);
                    }
                    else
                    {
                        MessageBox.Show("Error al subir la vacuna");
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión: " + ex.Message);
            }
        }

        private void CrearTarjetaVacuna(Vacuna vacuna)
        {
            Panel tarjeta = new Panel();
            tarjeta.Width = 450;
            tarjeta.Height = 200;
            tarjeta.BackColor = Color.White;
            tarjeta.BorderStyle = BorderStyle.None;

            int anchoPanel = flowLayoutPanelVacunas.ClientSize.Width;
            int margenIzquierdo = (anchoPanel - tarjeta.Width) / 2;
            if (margenIzquierdo < 0)
            {
                margenIzquierdo = 0;
            }
            tarjeta.Margin = new Padding(margenIzquierdo, 20, 0, 10);

            tarjeta.Resize += (s, e) => BordesRedondos(tarjeta, 20);

            Label lblNombre = new Label();
            lblNombre.Text = vacuna.name;
            lblNombre.Font = new Font("Segoe UI", 11, FontStyle.Bold);
            lblNombre.AutoSize = false;
            lblNombre.Width = tarjeta.Width - 20;
            lblNombre.Height = 25;
            lblNombre.Top = 10;
            lblNombre.Left = 10;

            Label lblFechas = new Label();
            lblFechas.Text = $"Administrada: {DateTime.Parse(vacuna.dateGiven):dd/MM/yyyy}   Próxima: {DateTime.Parse(vacuna.nextDate):dd/MM/yyyy}";
            lblFechas.Font = new Font("Segoe UI", 9, FontStyle.Italic);
            lblFechas.ForeColor = Color.Gray;
            lblFechas.AutoSize = false;
            lblFechas.Width = tarjeta.Width - 20;
            lblFechas.Height = 20;
            lblFechas.Top = lblNombre.Bottom + 2;
            lblFechas.Left = 10;

            Label lblNotes = new Label();
            lblNotes.Text = vacuna.notes;
            lblNotes.Font = new Font("Segoe UI", 10);
            lblNotes.AutoSize = false;
            lblNotes.Width = tarjeta.Width - 20;
            lblNotes.Height = 60;
            lblNotes.Top = lblFechas.Bottom + 5;
            lblNotes.Left = 10;

            Button btnEditar = new Button();
            btnEditar.Text = "Modificar";
            btnEditar.Width = 100;
            btnEditar.Height = 30;
            btnEditar.Left = 10;
            btnEditar.Top = lblNotes.Bottom + 10;
            btnEditar.BackColor = Color.LightBlue;

            btnEditar.Click += async (s, e) =>
            {
                VacunaDialog dialog = new VacunaDialog(
                    vacuna.name,
                    DateTime.Parse(vacuna.dateGiven),
                    DateTime.Parse(vacuna.nextDate),
                    vacuna.notes
                );

                if (dialog.ShowDialog() == DialogResult.OK)
                {
                    await ModificarVacuna(vacuna.id, dialog.Nombre, dialog.FechaAdministrada, dialog.FechaProxima, dialog.Notes);
                    await CargarVacunasDeMascota(vacuna.petId);
                }
            };

            Button btnEliminar = new Button();
            btnEliminar.Text = "Eliminar";
            btnEliminar.Width = 100;
            btnEliminar.Height = 30;
            btnEliminar.Left = tarjeta.Width - btnEliminar.Width - 10;
            btnEliminar.Top = lblNotes.Bottom + 10;
            btnEliminar.BackColor = Color.Red;

            btnEliminar.Click += async (s, e) =>
            {
                if (MessageBox.Show("¿Seguro que quieres eliminar esta vacuna?", "Confirmar", MessageBoxButtons.YesNo) == DialogResult.Yes)
                {
                    await EliminarVacuna(vacuna.id);
                    await CargarVacunasDeMascota(vacuna.petId);
                }
            };

            tarjeta.Controls.Add(lblNombre);
            tarjeta.Controls.Add(lblFechas);
            tarjeta.Controls.Add(lblNotes);
            tarjeta.Controls.Add(btnEditar);
            tarjeta.Controls.Add(btnEliminar);

            flowLayoutPanelVacunas.Controls.Add(tarjeta);
            flowLayoutPanelVacunas.Controls.SetChildIndex(tarjeta, 0);
        }

        private async Task CargarVacunasDeMascota(int petId)
        {
            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");

                    HttpResponseMessage response = await client.GetAsync("vaccines/pet/" + petId);

                    string json = await response.Content.ReadAsStringAsync();

                    if (response.IsSuccessStatusCode)
                    {
                        List<Vacuna> vacunas = Newtonsoft.Json.JsonConvert.DeserializeObject<List<Vacuna>>(json);

                        flowLayoutPanelVacunas.Controls.Clear();

                        foreach (var v in vacunas)
                        {
                            CrearTarjetaVacuna(v);
                        }
                    }
                    else
                    {
                        MessageBox.Show("Error al cargar vacunas\nStatus: " + (int)response.StatusCode + "\nBody: " + json);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión: " + ex.Message);
            }
        }

        private void BordesRedondos(Panel panel, int radio)
        {
            GraphicsPath path = new GraphicsPath();
            path.StartFigure();
            path.AddArc(new Rectangle(0, 0, radio, radio), 180, 90);
            path.AddArc(new Rectangle(panel.Width - radio, 0, radio, radio), 270, 90);
            path.AddArc(new Rectangle(panel.Width - radio, panel.Height - radio, radio, radio), 0, 90);
            path.AddArc(new Rectangle(0, panel.Height - radio, radio, radio), 90, 90);
            path.CloseFigure();
            panel.Region = new Region(path);
        }

        private async Task CargarMascotas()
        {
            try
            {
                using (HttpClient client = new HttpClient())
                {
                    client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");
                    HttpResponseMessage response = await client.GetAsync("pets/user/" + Sesion.UserId);
                    string json = await response.Content.ReadAsStringAsync();

                    if (response.IsSuccessStatusCode)
                    {
                        List<Mascota> mascotas = Newtonsoft.Json.JsonConvert.DeserializeObject<List<Mascota>>(json);

                        cmbMascotas.DataSource = mascotas;
                        cmbMascotas.DisplayMember = "name";
                        cmbMascotas.ValueMember = "id";
                    }
                    else
                    {
                        MessageBox.Show("Error al cargar mascotas\nStatus: " + (int)response.StatusCode + "\nBody: " + json);
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error de conexión: " + ex.Message);
            }
        }

        private async Task ModificarVacuna(int id, string nombre, DateTime fechaAdmin, DateTime fechaProx, string notes)
        {
            var vacuna = new
            {
                name = nombre,
                dateGiven = fechaAdmin.ToString("yyyy-MM-dd"),
                nextDate = fechaProx.ToString("yyyy-MM-dd"),
                notes = notes
            };

            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");
                string json = Newtonsoft.Json.JsonConvert.SerializeObject(vacuna);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                await client.PutAsync("vaccines/" + id, content);
            }
        }

        private async Task EliminarVacuna(int id)
        {
            using (HttpClient client = new HttpClient())
            {
                client.BaseAddress = new Uri("http://localhost:8080/apiawror/rest/");
                await client.DeleteAsync("vaccines/" + id);
            }
        }

    }
}
