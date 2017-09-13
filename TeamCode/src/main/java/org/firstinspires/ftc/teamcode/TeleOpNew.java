/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "Teleop Variable Flywheel Improved Drive")
public class TeleOpNew extends LinearOpMode {
    static DcMotor rFmotor, rBmotor, lFmotor, lBmotor;

    // RampFlywheel rampFlywheel = new RampFlywheel();
    // RampDownFlywheel rampDownFlywheel = new RampDownFlywheel();
    public void runOpMode() throws InterruptedException {
        double fwdPower, strafePower, rotationPower;

        rFmotor = hardwareMap.dcMotor.get("rF");
        rBmotor = hardwareMap.dcMotor.get("rB");
        lFmotor = hardwareMap.dcMotor.get("lF");
        lBmotor = hardwareMap.dcMotor.get("lB");
        lBmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rBmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lFmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rFmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lBmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rBmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rFmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        rBmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        lBmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        lFmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        double variableSpeed;
        waitForStart();

        while (opModeIsActive()) {
            fwdPower = gamepad1.left_stick_y;
            strafePower = gamepad1.left_stick_x;
            rotationPower = gamepad1.right_stick_x;

            if (gamepad1.start) {
                lFmotor.setPower((fwdPower + strafePower + rotationPower) / 2);
                lBmotor.setPower((fwdPower - strafePower + rotationPower) / 2);
                rFmotor.setPower((fwdPower - strafePower - rotationPower) / 2);
                rBmotor.setPower((fwdPower + strafePower - rotationPower) / 2);
            } else {
                double vD = Math.sqrt(Math.pow(fwdPower, 2) + Math.pow(strafePower, 2));
                double thetaD = Math.atan2(strafePower, fwdPower);
                double frontLeft = vD * Math.sin(thetaD + Math.PI / 4) + rotationPower;
                double frontRight = vD * Math.cos(thetaD + Math.PI / 4) - rotationPower;
                double backLeft = vD * Math.cos(thetaD + Math.PI / 4) + rotationPower;
                double backRight = vD * Math.sin(thetaD + Math.PI / 4) - rotationPower;
                double maxF = Math.max(Math.abs(frontLeft), Math.abs(frontRight));
                double maxB = Math.max(Math.abs(backLeft), Math.abs(backRight));
                double max = Math.max(maxF, maxB);
//
                if (max > 1.0) {
                    frontLeft = frontLeft / max;
                    frontRight = frontRight / max;
                    backLeft = backLeft / max;
                    backRight = backRight / max;
                }

                lFmotor.setPower(frontLeft);
                lBmotor.setPower(backLeft);
                rFmotor.setPower(frontRight);
                rBmotor.setPower(backRight);
            }
        }
    }
}