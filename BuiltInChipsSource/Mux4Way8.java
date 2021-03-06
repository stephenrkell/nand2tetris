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
 * This file was originally developed as part of the software suite that        *
 * supports the book "The Elements of Computing Systems" by Nisan and Schocken, *
 * MIT Press 2005. If you modify the contents of this file, please document and *
 * mark your changes clearly, for the benefit of others.                        *
 ********************************************************************************/

package builtInChips;

import Hack.Gates.BuiltInGate;

/**
 * 4-way 8-bit multiplexor.
 * The two sel[0..1] bits select the output to be one of the four input buses
 * (0->a ... 3->d).
 */
public class Mux4Way8 extends BuiltInGate {

    protected void reCompute() {
        byte a = (byte) inputPins[0].get();
        byte b = (byte) inputPins[1].get();
        byte c = (byte) inputPins[2].get();
        byte d = (byte) inputPins[3].get();
        byte sel = (byte) inputPins[4].get();
        byte out = 0;

        switch (sel) {
            case 0: out = a; break;
            case 1: out = b; break;
            case 2: out = c; break;
            case 3: out = d; break;
        }

        outputPins[0].set(out);
    }
}
