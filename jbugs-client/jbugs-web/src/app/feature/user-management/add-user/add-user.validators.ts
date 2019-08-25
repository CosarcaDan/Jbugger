import {AbstractControl, ValidationErrors} from '@angular/forms';

export class AddUserValidators {

  static validateName(control: AbstractControl): ValidationErrors | null {
    let value: string = control.value;
    let regexp =
      new RegExp('^[A-ZÜÄÖÂÎĂȚȘÁÉÓŐÚŰ][a-zA-Zșțăîâäöüßáéóőúű]{1,30}[- ]?[a-zșțăîâäöüáéóőúűßA-ZÜÄÖÂÎĂȚȘÁÉÓŐÚŰ]{0,30}[a-zșțăîâäöüßáéóőúű]$');
    if (value && !regexp.test(value)) {
      return {validateName: true};
    }
  }

  static validateNumber(control: AbstractControl): ValidationErrors | null {
    let value: string = control.value;
    let regexpGermany =
      new RegExp('^(\\+49)?1(5[12579]|6[023]|7[0-9])[0-9]{7}$');
    let regexpRomania =
      new RegExp('^(004|\\+4)?07[0-9]{8}$');
    if (value && (!regexpGermany.test(value) && !regexpRomania.test(value))) {
      return {validateNumber: true};
    }
  }

  static validateEmail(control: AbstractControl): ValidationErrors | null {
    let value: string = control.value;
    let regexp =
      new RegExp('^[a-zA-Z0-9-_.]*@msggroup\\.com$');
    if (value && !regexp.test(value)) {
      return {validateEmail: true};
    }
  }

  static cannotContainSpace(control: AbstractControl): ValidationErrors | null {
    let value: string = control.value;
    if (value && value.indexOf(' ') >= 0) {
      return {cannotContainSpace: true};
    }
  }
}
