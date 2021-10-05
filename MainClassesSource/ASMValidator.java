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
 * This file is based on code originally developed as part of software for      *
 * supports the book "The Elements of Computing Systems" by Nisan and Schocken, *
 * MIT Press 2005. Modified as follows:
 *
 *    by Stephen Kell: create ASMValidator from CPUEmulator                     *
 ********************************************************************************/

import Hack.Controller.*;
import Hack.CPUEmulator.*;
import Hack.Assembler.*;
import Hack.Translators.HackTranslatorException;
import Hack.Utilities.*;
import java.util.Set;
import java.util.TreeSet;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.String;

class MyHackAssembler extends HackAssembler
{
    MyHackAssembler(String fileName) throws HackTranslatorException
    {
        super(fileName, Definitions.ROM_SIZE, HackAssemblerTranslator.NOP, false);
    }
    // override to be more permissive
    protected void compileLine(String line) throws HackTranslatorException
    {
        try
        {
            super.compileLine(line);
        } catch (HackTranslatorException ex)
        {
            // try to continue: add a nop and carry on
            addCommand(HackAssemblerTranslator.NOP);
        }
    }
    public void validateProgram()
    {
        TreeSet distinctCodesSeen = new TreeSet();
        HackAssemblerTranslator at = HackAssemblerTranslator.getInstance();
        for (int i = 0; i < program.length; ++i)
        {
            short code = program[i];
            if (code != HackAssemblerTranslator.NOP)
            {
                System.out.println(Conversions.decimalToBinary(code, 16));
            }
            if (code >= 0) code = 0; // all A instructions are alike
            if (code == HackAssemblerTranslator.NOP) continue;
            boolean isNew = distinctCodesSeen.add(Short.valueOf(code));
            if (isNew)
            {
                String s = "(null)";
                try
                {
                    s = at.codeToText(program[i]);
                }
                catch (AssemblerException ex)
                {
                    System.err.println("Shouldn't happen: " + ex.toString());
                }
                System.err.println("Counted '" + s + "' as a distinct instruction");
            }
        }
    }
}

public class ASMValidator
{
  /**
   * The command line  ASM Validator program.
   */
  
  public static void main(String[] args) {
        if (args.length != 1)
            System.err.println("Usage: java HDLValidator [script name]");
        else try {
            // read the lines of the file, and
            // 
            //BufferedReader br = new BufferedReader(new FileReader(args[0]));
            //String line = br.readLine();
            MyHackAssembler a = new MyHackAssembler(args[0]);
            a.validateProgram();
            /*
            HackAssemblerTranslator at = HackAssemblerTranslator.getInstance();
            TreeSet distinctCodesSeen = new TreeSet();
            while (line != null)  
            {
                try
                {
                    short code = at.textToCode(line);
                    if (code >= 0) code = 0; // all A instructions are alike
                    boolean isNew = distinctCodesSeen.add(Short.valueOf(code));
                    if (isNew)
                    {
                        System.err.println("Counted '" + line + "' as a distinct instruction");
                    }
                }
                catch (AssemblerException ex)
                {
                    System.err.println("Assembler said: " + ex.toString());

                }
                line = br.readLine();  
            }*/
        }
        //catch (IOException ex)
        //{
        //    System.err.println("Error reading '" + args[0] + "': " + ex.toString());
        //   System.exit(1);
        //}
        catch (HackTranslatorException ex)
        {
            System.err.println("Error translating '" + args[0] + "': " + ex.toString());
            System.exit(2);
        }
    }
}
