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

  static validateFileExtension(control: AbstractControl): ValidationErrors | null {
    let value: string = control.value;
    let regexExtension = new RegExp('([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.pdf|.doc|.odf|.xlsx|.xls|.png|.jpg)$');
    if (value && (!regexExtension.test(value))) {
      return {validateFileExtension: true};
    }
  }
}
