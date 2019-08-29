import {AbstractControl, ValidationErrors} from '@angular/forms';

export class BugValidators {
  static cannotContainSpace(control: AbstractControl): ValidationErrors | null {
    let value: string = control.value;
    if (value && value.indexOf(' ') >= 0) {
      return {cannotContainSpace: true};
    }
  }

  static validateVersion(control: AbstractControl): ValidationErrors | null {
    let value: string = control.value;
    let regexpVersion =
      new RegExp('^(0|[1-9a-zA-Z][0-9a-zA-z]?)\\.(0|[1-9a-zA-Z][0-9a-zA-Z]?)\\.(0|[1-9a-zA-Z][0-9a-zA-Z]?)$');
    if (value && (!regexpVersion.test(value))) {
      return {validateVersion: true};
    }
  }
}
