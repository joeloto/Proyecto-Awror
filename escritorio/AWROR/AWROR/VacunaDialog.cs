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
    public partial class VacunaDialog : Form
    {
        public string Nombre { get; set; }
        public DateTime FechaAdministrada { get; set; }
        public DateTime FechaProxima { get; set; }
        public string Notes { get; set; }

        public VacunaDialog()
        {
            InitializeComponent();
        }

        public VacunaDialog(string nombre, DateTime fechaAdmin, DateTime fechaProx, string notes)
        {
            InitializeComponent();
            txtNombre.Text = nombre;
            dtpFechaAdmin.Value = fechaAdmin;
            dtpFechaProxima.Value = fechaProx;
            txtNotes.Text = notes;
        }

        private void btnAceptar_Click(object sender, EventArgs e)
        {
            Nombre = txtNombre.Text;
            FechaAdministrada = dtpFechaAdmin.Value;
            FechaProxima = dtpFechaProxima.Value;
            Notes = txtNotes.Text;
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
