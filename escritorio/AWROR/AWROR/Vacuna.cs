using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AWROR
{
    public class Vacuna
    {
        public int id { get; set; }
        public int petId { get; set; }
        public string name { get; set; }
        public string dateGiven { get; set; }
        public string nextDate { get; set; }
        public string notes { get; set; }
    }


}
