/********************************************************************************
 * The contents of this file are subject to the GNU General Public License      *
 * (GPL) Version 2 or later (the "License"); you may not use this file except   *
 * in compliance with the License. You may obtain a copy of the License at      *
 * http://www.gnu.org/copyleft/gpl.html                                         *
 *                                                                              *
 * Software distributed under the License is distributed on an "AS IS" basis,   *
 * without warranty of any kind, either expressed or implied. See the License   *
 * for the specific language governing rights and limitations under the         *
 * License.                                                                     *
 *                                                                              *
 * This file is BASED ON a file originally developed as part of software that   *
 * supports the book "The Elements of Computing Systems" by Nisan and Schocken, *
 * MIT Press 2005. If you modify the contents of this file, please document and *
 * mark your changes clearly, for the benefit of others.                        *
 *
 * changed by Stephen Kell: new file HDLValidator.java
 *        based on HardwareSimulatorMain.java
 *        but doing syntactic and semantic validation rather than simulation    *
 ********************************************************************************/

import Hack.Controller.*;
import Hack.HardwareSimulator.*;
import SimulatorsGUI.*;
import java.util.Vector;
import javax.swing.*;
import Hack.Gates.*;


/**
 * The Hardware Simulator.
 */
public class HDLValidator extends HardwareSimulator
{
  /**
   * The command line Hardware Simulator program.
   */
  public static void main(java.lang.String[] args) {
        if (args.length != 1)
            System.err.println("Usage: java HDLValidator <HDL file name>");
        else { /*new HackController(new HardwareSimulator(), args[0]);*/
        
        /* What does the above line do?
         *   the new HardwareSimulator is the engine that can simulate hardware
         *      but doesn't seem to parse the file except during a rowSelected event
         *   it creates a Script instance using args[0]
         *   and when it sees COMMAND_LOAD ("load") in the script
         *   it does loadGate
         *   ... so let's just loadGate
         *   */
         
            HDLValidator val = new HDLValidator();
            
            try {
                val.loadGate(args[0], true);
            }
            catch (GateException ex) {
                System.err.println(ex.toString());
                System.exit(1);
            }
            // print out the PARTS list of the HDL
            Gate g = val.getGate();
            GateClass gc = g.getGateClass(); 
            if (gc instanceof CompositeGateClass) {
                Vector partsList = ((CompositeGateClass) gc).getPartsList();
                for (int i = 0; i < partsList.size(); ++i) {
                    GateClass partGc = (GateClass) partsList.get(i);
                    System.out.println(partGc.getName());
                }
            }
         }
         System.exit(0);
    }
    
    public void loadGate(String fileName) throws GateException
    {
        super.loadGate(fileName, true);
    }
}
