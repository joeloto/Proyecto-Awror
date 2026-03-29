using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace AWROR
{
    public partial class MascotaDialog : Form
    {
        public string Nombre { get; private set; }
        public string Tipo { get; private set; }
        public Image ImagenSeleccionada { get; private set; }

        public MascotaDialog()
        {
            InitializeComponent();
        }

        public MascotaDialog(string nombre, string tipo, Image imagen)
        {
            InitializeComponent();
            txtNombre.Text = nombre;
            txtTipo.Text = tipo;
            pbPreFoto.Image = imagen;
            ImagenSeleccionada = imagen;
        }


        private void btnSeleccionarImagen_Click(object sender, EventArgs e)
        {
            OpenFileDialog ofd = new OpenFileDialog();
            ofd.Filter = "Imágenes|*.jpg;*.png;*.jpeg";

            if (ofd.ShowDialog() == DialogResult.OK)
            {
                ImagenSeleccionada = Image.FromFile(ofd.FileName);
                pbPreFoto.Image = ImagenSeleccionada;
            }
        }

        private void btnAgregar_Click(object sender, EventArgs e)
        {
            Nombre = txtNombre.Text;
            Tipo = txtTipo.Text;
            this.DialogResult = DialogResult.OK;
            this.Close();
        }

        private void btnCancelar_Click(object sender, EventArgs e)
        {
            this.DialogResult = DialogResult.Cancel;
            this.Close();
        }
    }

}
