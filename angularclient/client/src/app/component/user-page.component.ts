import { Component, OnInit, OnChanges } from '@angular/core'
import { FormGroup, FormBuilder, Validators } from '@angular/forms'
import { UserService } from '../service/user.service'

@Component({
	selector: 'user-page',
	templateUrl: '../template/user-page.component.html'
})
export class UserPageComponent implements OnInit {

    public visible = false;
    
    public unmatchingPasswords = false;
    public oldPasswordNotEntered = false;

    public changePasswordForm = this.formBuilder.group({
        oldpassword: [''], 
        newpassword: [''],
        confirmpassword: ['']
    },
    {
        validator: this.validatePassword
    });

    changePasswordExpanded = false;

    constructor(private userService: UserService, private formBuilder: FormBuilder){

    }

    validatePassword(formGroup: FormGroup) {
        let oldpassword = formGroup.controls['oldpassword'];
        let newpassword = formGroup.controls['newpassword'];
        let confirmpassword = formGroup.controls['confirmpassword'];

        formGroup.setErrors({
            oldpasswordNotEnteredError: oldpassword.value.length < 1,
            passwordConfirmError: newpassword.value != confirmpassword.value 
        });
        
        return formGroup.errors;
    }


    ngOnInit() {
        this.userService.userPageVisible.subscribe(value => {
            this.visible = value;
        });
    }

    submitChangePassword(event) {
        this.unmatchingPasswords = this.changePasswordForm.errors['passwordConfirmError'];
        this.oldPasswordNotEntered = this.changePasswordForm.errors['oldpasswordNotEnteredError'];
        console.log(this.changePasswordForm);
    }

    toggleUserPage():void {
        this.userService.toggleUserPage();
    }
}
