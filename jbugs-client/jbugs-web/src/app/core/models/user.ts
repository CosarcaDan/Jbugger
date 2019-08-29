export interface User {
  id: number;
  failedLoginAttempt: number;
  firstName: string;
  lastName: string;
  email: string;
  mobileNumber: string;
  password: string;
  username: string;
  status: boolean;
}
