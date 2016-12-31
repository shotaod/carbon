package org.carbon.web.def;

/**
 * @author Shota Oda 2016/10/09.
 */
public class Logo {
    public final String RESET = "\u001B[0m";
    public final String BLACK = "\u001B[30m";
    public final String WHITE = "\u001B[37m";
    public final String RED = "\u001B[31m";
    public final String GREEN = "\u001B[32m";
    public final String YELLOW = "\u001B[33m";
    public final String BLUE = "\u001B[34m";
    public final String PURPLE = "\u001B[35m";
    public final String CYAN = "\u001B[36m";
	public final String logo = "\n                      {B1}@@@\n                     @@ @@\n             {R}{B2}@@@      {B1}@@@\n            {B2}@@ @@\n             @@@{R}         {B3}@@@@@\n                        @@   @@\n                       @@     @@\n                        @@   @@\n                         @@@@@{R}\n          @@@@@@@@@@@@@@@@@@@@@@@@@\n        .@@@@@@@@@@@@@@@@@@@@@@@@@@@.\n       8@@@@                     @@@@8\n      @@@@@    {HR}@@@@@@@@@@@@@@@@@@@{R}@@@@@\n    :@@@0                           0@@@:\n    :@@@0{W}                  @@@@@@@@@{R}@@@@:\n    :@@@0{W}               @@@@@@@@@@@@{R}@@@@:\n    :@@@0{W}              @@@@@        {R}0@@@:\n    :@@@0{W}             @@@@          {R}0@@@:\n    :@@@0{W}            @@@@           {R}0@@@:\n    :@@{T}  ___   _   ___ ___  ___  _  _ {R}@@:\n    :@@{T} / __| /_\\ | _ | _ )/ _ \\| \\| |{R}@@:\n    :@@{T}| (__ / _ \\|   | _ | (_) | .` |{R}@@:\n    :@@{T} \\___/_/ \\_|_|_|___/\\___/|_|\\_|{R}@@:\n    :@@                               @@:\n    :@@@0{W}          @@@@             {R}0@@@:\n    :@@@0{W}         @@@@              {R}0@@@:\n    :@@@0{W}      @@@@@@               {R}0@@@:\n    :@@@@{W}@@@@@@@@@@                 {R}0@@@:\n    :@@@@{W}@@@@@@                     {R}0@@@:\n    :@@@0                           0@@@:\n    :@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@;\n      @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n       .@@@@                      @@@@.\n         @@@@@@@@@@@@@@@@@@@@@@@@@@@;\n"
            .replace("{B1}", YELLOW)
            .replace("{B2}", GREEN)
            .replace("{B3}", CYAN)
            .replace("{R}", RESET)
            .replace("{T}", BLACK)
            .replace("{HR}", "")
            .replace("{W}", RED);
}
